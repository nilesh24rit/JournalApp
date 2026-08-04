package com.nilesh.JournalingApp.ServiceTest;

import com.nilesh.JournalingApp.Entity.User;
import com.nilesh.JournalingApp.Repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Disabled
    @ParameterizedTest
    @CsvSource({
            "ram",
            "Nilesh",
            "Shyam",
    })
    public void findByUsernameTest(String name){
        assertNotNull( userRepository.findByUsername(name),"Failed for"+name);
    }
    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,2,3,",
            "2,3,4"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected,a+b);
    }
}
