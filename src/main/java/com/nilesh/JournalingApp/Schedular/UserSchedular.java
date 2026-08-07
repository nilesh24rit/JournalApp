package com.nilesh.JournalingApp.Schedular;

import com.nilesh.JournalingApp.Cache.AppCache;
import com.nilesh.JournalingApp.Entity.JournalEntry;
import com.nilesh.JournalingApp.Entity.User;
import com.nilesh.JournalingApp.Repository.UserRepositoryImpl;
import com.nilesh.JournalingApp.Service.EmailService;
import com.nilesh.JournalingApp.enums.Sentiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserSchedular {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryimpl;


    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSamMil() {

        List<User> users = userRepositoryimpl.getUsersforSA();

        for (User user : users) {

            List<JournalEntry> journalEntries = user.getJournalEntries();

            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(JournalEntry::getSentiment)
                    .collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for (Sentiment sentiment : sentiments) {
                if (sentiment != null) {
                    sentimentCounts.put(
                            sentiment,
                            sentimentCounts.getOrDefault(sentiment, 0) + 1
                    );
                }
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;

            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if (mostFrequentSentiment != null) {
                emailService.sendmail(
                        user.getEmail(),
                        "Sentiment for last 7 days",
                        mostFrequentSentiment.toString()
                );
            }
        }
    }
    @Scheduled(cron = "0 */5 * * * *")
    public void clearAppCache() {
        appCache.init();
    }
}