package com.nilesh.JournalingApp.Controller;

import com.nilesh.JournalingApp.Service.GoogleAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/google")
@Slf4j
public class GoogleAuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> googleLogin(@RequestParam String idToken) {
        return googleAuthService.handleGoogleLogin(idToken);
    }
}