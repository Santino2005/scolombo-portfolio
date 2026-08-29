package com.geno_insights.scolombo.config;

import com.geno_insights.scolombo.guard.model.entity.Guard;
import com.geno_insights.scolombo.guard.repository.GuardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private GuardRepository guardRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void run_WhenAdminDoesNotExist_SeedsAdmin() {
        when(guardRepository.findByUserName("admin")).thenReturn(Optional.empty());

        dataInitializer.run();

        verify(guardRepository).save(any(Guard.class));
    }

    @Test
    void run_WhenAdminAlreadyExists_DoesNotSeed() {
        when(guardRepository.findByUserName("admin")).thenReturn(Optional.of(new Guard()));

        dataInitializer.run();

        verify(guardRepository, never()).save(any(Guard.class));
    }
}
