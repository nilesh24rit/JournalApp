package com.nilesh.JournalingApp.Repository;

import com.nilesh.JournalingApp.Entity.ConfigJournalAppEntity;
import com.nilesh.JournalingApp.Entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, String> {


}
