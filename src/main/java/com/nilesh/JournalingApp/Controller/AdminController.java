package com.nilesh.JournalingApp.Controller;

import com.nilesh.JournalingApp.Entity.User;
import com.nilesh.JournalingApp.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/admin")
@Tag(name="Admin APIs", description = "Creating and adding admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getallUsers(){
        List<User> all = userService.getAll();
        if(all !=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("create-new-admin")
    public void createAdmin(@RequestBody User user){
        userService.saveAdmin(user);
    }
}
