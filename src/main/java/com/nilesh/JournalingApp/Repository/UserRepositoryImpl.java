package com.nilesh.JournalingApp.Repository;

import com.nilesh.JournalingApp.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


class UserRepositoryImplComponent {

}
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersforSA(){
        Query query = new Query();
        query.addCriteria(Criteria.where("emial").is(true));
        query.addCriteria(Criteria.where("emial").ne("").ne(null));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;


    }
}
