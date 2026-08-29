package com.uberclocked.api.emailData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

  @Mock private JavaMailSender mailSender;

  private EmailService emailService;

  @BeforeEach
  void setUp() {
    emailService = new EmailService(mailSender);
  }

  @Test
  void sendMail_sendsSimpleMailMessage() {
    emailService.sendMail("to@test.com", "Subject", "Body");

    ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
    verify(mailSender).send(captor.capture());

    SimpleMailMessage message = captor.getValue();
    assertEquals("to@test.com", message.getTo()[0]);
    assertEquals("Subject", message.getSubject());
    assertEquals("Body", message.getText());
  }

  @Test
  void sendToMany_whenListValid_sendsMail() {
    List<String> recipients = List.of("a@test.com", "b@test.com");
    emailService.sendToMany(recipients, "Subject", "Body");

    ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
    verify(mailSender).send(captor.capture());

    SimpleMailMessage message = captor.getValue();
    assertEquals(2, message.getTo().length);
    assertEquals("Subject", message.getSubject());
  }

  @Test
  void sendToMany_whenNullOrEmpty_doesNothing() {
    emailService.sendToMany(null, "Subject", "Body");
    emailService.sendToMany(Collections.emptyList(), "Subject", "Body");

    verify(mailSender, never()).send(any(SimpleMailMessage.class));
  }
}
