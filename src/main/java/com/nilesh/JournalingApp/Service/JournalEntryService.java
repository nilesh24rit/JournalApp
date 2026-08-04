package com.nilesh.JournalingApp.Service;

import com.nilesh.JournalingApp.Entity.JournalEntry;
import com.nilesh.JournalingApp.Entity.User;
import com.nilesh.JournalingApp.Repository.JournalRepository;
import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalRepository journalEntryRepository;
    @Autowired
    private UserService userService;



    @Transactional
    public void save(JournalEntry journalEntry, String username){
        try {
            User user=userService.findbyUsername(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved=journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occoured while saving the entry.",e);
        }
    } 

    public void save(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deletebyid(ObjectId id, String username){
        boolean removed=false;
        try {
            User user=userService.findbyUsername(username);
            removed =user.getJournalEntries().removeIf(x->x.getId().equals(id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occoured while deleting the entry");
        }
        return removed;
    }
}
