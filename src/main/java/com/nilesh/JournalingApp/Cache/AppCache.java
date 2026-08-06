package com.nilesh.JournalingApp.Cache;

import com.nilesh.JournalingApp.Entity.ConfigJournalAppEntity;
import com.nilesh.JournalingApp.Repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {
    public Map<String,String> APP_CACHE =new HashMap<>();

    @Autowired
    private ConfigJournalAppRepository  configJournalAppRepository;

    @PostConstruct
    public void init(){
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity configJournalAppEntity:all){
            APP_CACHE.put(configJournalAppEntity.getKey(), configJournalAppEntity.getValue());
        }

    }
}
