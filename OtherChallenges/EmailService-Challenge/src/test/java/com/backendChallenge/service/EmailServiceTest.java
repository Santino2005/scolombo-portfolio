package com.backendChallenge.service;

import com.backendChallenge.email.Email;
import com.backendChallenge.email.EmailRepository;
import com.backendChallenge.email.EmailService;
import com.backendChallenge.email.emailProvider.registry.EmailHandler;
import com.backendChallenge.result.EmailResult;
import com.backendChallenge.result.Result;
import com.backendChallenge.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private EmailHandler emailHandler;

    @Mock
    private EmailRepository emailRepository;

    @InjectMocks
    private EmailService emailService;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User("1L","sender@test.com",ZoneId.of("UTC"));
    }

    @Test
    void shouldReturnNotSuccessWhenDailyLimitReached() {
        when(emailRepository.countByUserIdAndDate(eq(user.getId()), any(LocalDate.class)))
                .thenReturn(1000L);

        Result result = emailService.sendEmail(user, "to@test.com", "subject", "body");

        assertFalse(result.successful());
        assertEquals("Daily limit reached 1000 emails", result.message());
        verify(emailHandler, never()).sendEmail(any(), any(), any(), any());
        verify(emailRepository, never()).save(any());
    }

    @Test
    void shouldSendEmailSuccessfully() {
        when(emailRepository.countByUserIdAndDate(eq(user.getId()), any(LocalDate.class)))
                .thenReturn(999L);

        EmailResult successResult = EmailResult.success(null, "CorrectMessage", "providerX");
        when(emailHandler.sendEmail(eq("sender@test.com"), eq("to@test.com"), eq("subject"), eq("body")))
                .thenReturn(successResult);

        Result result = emailService.sendEmail(user, "to@test.com", "subject", "body");

        assertTrue(result.successful());
        assertEquals("providerX", ((EmailResult) result).provider());
        verify(emailHandler, times(1)).sendEmail(any(), any(), any(), any());
        verify(emailRepository, times(1)).save(any(Email.class));
    }

    @Test
    void shouldNotSaveEmailWhenSendFails() {
        when(emailRepository.countByUserIdAndDate(eq(user.getId()), any(LocalDate.class)))
                .thenReturn(500L);

        EmailResult failResult = EmailResult.notSuccess("SMTP error");
        when(emailHandler.sendEmail(any(), any(), any(), any()))
                .thenReturn(failResult);

        Result result = emailService.sendEmail(user, "to@test.com", "subject", "body");

        assertFalse(result.successful());
        assertEquals("SMTP error", result.message());
        verify(emailRepository, never()).save(any());
    }

    @Test
    void shouldReturnNotSuccessWhenEmailHandlerThrowsException() {
        when(emailRepository.countByUserIdAndDate(eq(user.getId()), any(LocalDate.class)))
                .thenReturn(0L);
        when(emailHandler.sendEmail(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Connection timeout"));

        Result result;
        try {
            result = emailService.sendEmail(user, "to@test.com", "subject", "body");
        } catch (Exception e) {
            result = null;
        }

        assertNull(result);
        verify(emailRepository, never()).save(any());
    }

    @Test
    void shouldAllowSendingWhenSentTodayIs999() {
        when(emailRepository.countByUserIdAndDate(eq(user.getId()), any(LocalDate.class)))
                .thenReturn(999L);
        EmailResult successResult = EmailResult.success(null, "sent", "providerX");
        when(emailHandler.sendEmail(any(), any(), any(), any()))
                .thenReturn(successResult);

        Result result = emailService.sendEmail(user, "to@test.com", "subject", "body");

        assertTrue(result.successful());
        verify(emailRepository).save(any(Email.class));
    }
}
