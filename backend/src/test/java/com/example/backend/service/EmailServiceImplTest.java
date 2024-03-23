package com.example.backend.service;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.example.backend.services.EmailServiceImpl;

public class EmailServiceImplTest {

    @Mock
    private JavaMailSenderImpl mailSender;

    private EmailServiceImpl emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl(mailSender);
    }

    @Test
    public void testSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        emailService.sendEmail(to, subject, text); // Call the method under test

        verify(mailSender, times(1)).send(argThat((SimpleMailMessage message) ->
                to.equals(message.getTo()[0])
                        && subject.equals(message.getSubject())
                        && text.equals(message.getText())
                        && message.getTo() != null
                        && message.getSubject() != null
                        && message.getText() != null
        ));
    }
}