package com.nilesh.JournalingApp.Controller;


import com.nilesh.JournalingApp.Entity.User;
import com.nilesh.JournalingApp.Repository.UserRepository;
import com.nilesh.JournalingApp.Service.UserDetailsServiceImpl;
import com.nilesh.JournalingApp.Utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/google")
@Slf4j
public class GoogleAuthController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<?> googleLogin(@RequestParam String idToken) {

        try {

            // Verify Google ID Token
            String userInfoUrl =
                    "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

            ResponseEntity<Map> userInfoResponse =
                    restTemplate.getForEntity(userInfoUrl, Map.class);


            if (userInfoResponse.getStatusCode() != HttpStatus.OK
                    || userInfoResponse.getBody() == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid Google ID Token");
            }


            Map<String, Object> userInfo = userInfoResponse.getBody();

            String email = (String) userInfo.get("email");


            if (email == null || email.isEmpty()) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Email not found in Google token");
            }


            try {

                userDetailsService.loadUserByUsername(email);

            } catch (Exception e) {

                User user = new User();

                user.setEmail(email);
                user.setUsername(email);

                user.setPassword(
                        passwordEncoder.encode(UUID.randomUUID().toString())
                );

                user.setRoles(Arrays.asList("USER"));

                userRepository.save(user);
            }


            // Generate your own JWT
            String jwtToken = jwtUtil.generateToken(email);


            return ResponseEntity.ok(
                    Collections.singletonMap("token", jwtToken)
            );

        } catch (Exception e) {

            log.error(
                    "Exception occurred while handling Google Login",
                    e
            );

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Google authentication failed");
        }
    }
}
