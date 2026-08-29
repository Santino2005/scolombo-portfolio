package com.geno_insights.scolombo.guard.service;

import com.geno_insights.scolombo.guard.model.entity.Guard;
import com.geno_insights.scolombo.guard.repository.GuardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuardServiceTest {

    @Mock
    private GuardRepository guardRepository;

    @InjectMocks
    private GuardService guardService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private Guard testGuard;

    @BeforeEach
    void setUp() throws Exception {
        testGuard = new Guard();
        testGuard.setUserName("guardAdmin");
        testGuard.setHashedPin(encoder.encode("1234"));

        Field idField = Guard.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(testGuard, UUID.randomUUID());
    }

    @Test
    void login_Success() {
        when(guardRepository.findByUserName("guardAdmin")).thenReturn(Optional.of(testGuard));

        String result = guardService.login("guardAdmin", "1234");
        assertEquals("Login successful", result);
        verify(guardRepository).findByUserName("guardAdmin");
    }

    @Test
    void login_InvalidPin_ThrowsException() {
        when(guardRepository.findByUserName("guardAdmin")).thenReturn(Optional.of(testGuard));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                guardService.login("guardAdmin", "wrongPin")
        );
        assertEquals("Invalid pin", exception.getMessage());
    }

    @Test
    void login_GuardNotFound_ThrowsException() {
        when(guardRepository.findByUserName("unknown")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                guardService.login("unknown", "1234")
        );
        assertEquals("Guard not found", exception.getMessage());
    }
}
