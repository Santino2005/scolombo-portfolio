package com.geno_insights.scolombo.visitor.service;

import com.geno_insights.scolombo.storage.SupabaseStorageService;
import com.geno_insights.scolombo.visitor.model.dto.CreateVisitorDto;
import com.geno_insights.scolombo.visitor.model.entity.Sector;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import com.geno_insights.scolombo.visitor.repository.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private SupabaseStorageService storageService;

    @InjectMocks
    private VisitorService visitorService;

    private Visitor testVisitor;

    @BeforeEach
    void setUp() {
        testVisitor = new Visitor("40123456", "Juan Perez", "Tech Corp", Sector.Operaciones, "https://photo.url/1.jpg");
    }

    @Test
    void findByDni_Success() {
        when(visitorRepository.findByDni("40123456")).thenReturn(Optional.of(testVisitor));

        Visitor result = visitorService.findByDni("40123456");
        assertNotNull(result);
        assertEquals("40123456", result.getDni());
        assertEquals("Juan Perez", result.getFullName());
    }

    @Test
    void findByDni_NotFound_ThrowsException() {
        when(visitorRepository.findByDni("99999999")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                visitorService.findByDni("99999999")
        );
        assertEquals("Visitor not found", ex.getMessage());
    }

    @Test
    void registerVisitor_Success() {
        MockMultipartFile photo = new MockMultipartFile("photo", "test.jpg", "image/jpeg", "imageContent".getBytes());
        CreateVisitorDto dto = new CreateVisitorDto("40123456", "Juan Perez", "Tech Corp", Sector.Operaciones, photo);

        when(storageService.uploadVisitorPhoto(photo)).thenReturn("https://photo.url/new.jpg");
        when(visitorRepository.save(any(Visitor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Visitor saved = visitorService.registerVisitor(dto);
        assertNotNull(saved);
        assertEquals("40123456", saved.getDni());
        assertEquals("https://photo.url/new.jpg", saved.getPhotoUrl());
        verify(storageService).uploadVisitorPhoto(photo);
        verify(visitorRepository).save(any(Visitor.class));
    }

    @Test
    void countVisitors_Success() {
        when(visitorRepository.count()).thenReturn(42L);

        long count = visitorService.countVisitors();
        assertEquals(42L, count);
        verify(visitorRepository).count();
    }
}
