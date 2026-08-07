package com.nilesh.JournalingApp.ServiceTest;


import com.nilesh.JournalingApp.Service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void setEmailService(){
        emailService.sendmail("artistnilesh24@gmail.com",
                "Java Testing Mail",
                "Hi aap kaise hai ");
    }


}
