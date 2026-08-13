package com.nilesh.JournalingApp.Controller;

import com.nilesh.JournalingApp.Entity.JournalEntry;
import com.nilesh.JournalingApp.Entity.User;
import com.nilesh.JournalingApp.Service.JournalEntryService;
import com.nilesh.JournalingApp.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Journal")
@Tag(name="Journal Entry APIs", description = "Read all, Entry, Delete by ID, Update by ID")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesofUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        System.out.println("Username from URL = " + username);
        User user = userService.findbyUsername(username);
        System.out.println("User object = " + user);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user.getJournalEntries());
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username=authentication.getName();
            journalEntryService.save(myEntry,username);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryByID(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user = userService.findbyUsername(username);
        List<JournalEntry> collect =  user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional<JournalEntry> journalEntry=journalEntryService.findById(myId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        boolean removed = journalEntryService.deletebyid(myId, username);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user = userService.findbyUsername(username);
        List<JournalEntry> collect =  user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional<JournalEntry> journalEntry=journalEntryService.findById(myId);
            if(journalEntry.isPresent()){
                JournalEntry old =journalEntry.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ?
                        newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getTitle().equals("") ?
                        newEntry.getContent() : old.getContent());
                journalEntryService.save(old, username);
                return new ResponseEntity<>(old, HttpStatus.OK);            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}