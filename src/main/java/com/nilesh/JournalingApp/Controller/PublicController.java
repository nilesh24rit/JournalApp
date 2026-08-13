package com.nilesh.JournalingApp.Controller;

import com.nilesh.JournalingApp.Entity.User;
import com.nilesh.JournalingApp.Service.UserService;
import com.nilesh.JournalingApp.Utils.JWTUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@Tag(name="Public APIs", description = "Signup and Login")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwTutil;

    @GetMapping
    public List<User> getAll(){
        return userService.getAll();
    }
    @PostMapping("/signup")
    public void signup(@RequestBody User user){
        userService.saveNewUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            return jwTutil.generateToken(userDetails.getUsername());

        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }


}
