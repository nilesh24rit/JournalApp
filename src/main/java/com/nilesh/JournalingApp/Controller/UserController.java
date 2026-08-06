package com.nilesh.JournalingApp.Controller;
import com.nilesh.JournalingApp.Entity.User;
import com.nilesh.JournalingApp.Repository.UserRepository;
import com.nilesh.JournalingApp.Service.UserService;
import com.nilesh.JournalingApp.Service.WeatherService;
import com.nilesh.JournalingApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WeatherService weatherService;


    @PutMapping
    public ResponseEntity<?> UpdateUser(@RequestBody User user){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User userInDB=userService.findbyUsername(username);
        userInDB.setUsername(user.getUsername());
        userInDB.setPassword((user.getPassword()));
        userService.saveNewUser(userInDB);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> DeleteUSer(@RequestBody User user){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        userRepository.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greeting(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        String greeting ="";
        if(weatherResponse!=null){
            greeting = ", Weather today feels like " + weatherResponse.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hi "+authentication.getName()+ greeting,HttpStatus.OK);
    }

}