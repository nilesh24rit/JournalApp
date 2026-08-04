package com.nilesh.JournalingApp.Controller;

import com.nilesh.JournalingApp.Entity.User;
import com.nilesh.JournalingApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll(){
        return userService.getAll();
    }
    @PostMapping
    public void GetUser(@RequestBody User user){
        userService.saveNewUser(user);
    }
}
