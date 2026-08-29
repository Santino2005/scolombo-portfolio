package com.geno_insights.scolombo.visit.service;

import com.geno_insights.scolombo.visit.model.entity.Visit;
import com.geno_insights.scolombo.visit.repository.VisitRepository;
import com.geno_insights.scolombo.visitor.model.entity.Sector;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import com.geno_insights.scolombo.visitor.service.VisitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private VisitorService visitorService;

    @InjectMocks
    private VisitService visitService;

    private Visitor testVisitor;
    private Visit testVisit;

    @BeforeEach
    void setUp() {
        testVisitor = new Visitor("40123456", "Juan Perez", "Tech Corp", Sector.Operaciones, "https://photo.url/1.jpg");
        testVisit = new Visit();
        testVisit.setVisitor(testVisitor);
        testVisit.setSector(Sector.Operaciones);
        testVisit.setQrToken(UUID.randomUUID().toString());
        testVisit.setEntryTime(LocalDateTime.now());
    }

    @Test
    void registerEntry_Success() {
        when(visitorService.findByDni("40123456")).thenReturn(testVisitor);
        when(visitRepository.findByVisitorAndExitTimeIsNull(testVisitor)).thenReturn(Optional.empty());
        when(visitRepository.save(any(Visit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Visit result = visitService.registerEntry("40123456", Sector.Operaciones);
        assertNotNull(result);
        assertEquals(testVisitor, result.getVisitor());
        assertEquals(Sector.Operaciones, result.getSector());
        assertNotNull(result.getQrToken());
        assertNotNull(result.getEntryTime());
    }

    @Test
    void registerEntry_AlreadyInside_ThrowsException() {
        when(visitorService.findByDni("40123456")).thenReturn(testVisitor);
        when(visitRepository.findByVisitorAndExitTimeIsNull(testVisitor)).thenReturn(Optional.of(testVisit));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                visitService.registerEntry("40123456", Sector.Operaciones)
        );
        assertEquals("Visitor already inside", ex.getMessage());
    }

    @Test
    void registerExit_Success() {
        String token = testVisit.getQrToken();
        when(visitRepository.findByQrToken(token)).thenReturn(Optional.of(testVisit));
        when(visitRepository.save(any(Visit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Visit closed = visitService.registerExit(token);
        assertNotNull(closed.getExitTime());
        verify(visitRepository).save(testVisit);
    }

    @Test
    void registerExit_VisitNotFound_ThrowsException() {
        when(visitRepository.findByQrToken("invalid-token")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                visitService.registerExit("invalid-token")
        );
        assertEquals("Visit not found", ex.getMessage());
    }

    @Test
    void registerExit_AlreadyClosed_ThrowsException() {
        testVisit.setExitTime(LocalDateTime.now());
        String token = testVisit.getQrToken();
        when(visitRepository.findByQrToken(token)).thenReturn(Optional.of(testVisit));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                visitService.registerExit(token)
        );
        assertEquals("Visit already closed", ex.getMessage());
    }

    @Test
    void getCredential_Success() {
        String token = testVisit.getQrToken();
        when(visitRepository.findByQrToken(token)).thenReturn(Optional.of(testVisit));

        Visit result = visitService.getCredential(token);
        assertEquals(testVisit, result);
    }

    @Test
    void getCredential_NotFound_ThrowsException() {
        when(visitRepository.findByQrToken("non-existent")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                visitService.getCredential("non-existent")
        );
        assertEquals("Visit not found", ex.getMessage());
    }

    @Test
    void getTodayVisits_Success() {
        when(visitRepository.findByEntryTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(testVisit));

        List<Visit> list = visitService.getTodayVisits();
        assertEquals(1, list.size());
    }

    @Test
    void getHistory_Success() {
        when(visitRepository.findAll()).thenReturn(List.of(testVisit));

        List<Visit> list = visitService.getHistory();
        assertEquals(1, list.size());
    }

    @Test
    void getActiveCredentialByDni_Success() {
        when(visitRepository.findByVisitorDniAndExitTimeIsNull("40123456")).thenReturn(Optional.of(testVisit));

        Visit result = visitService.getActiveCredentialByDni("40123456");
        assertEquals(testVisit, result);
    }

    @Test
    void getActiveCredentialByDni_NotFound_ThrowsException() {
        when(visitRepository.findByVisitorDniAndExitTimeIsNull("999")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                visitService.getActiveCredentialByDni("999")
        );
        assertEquals("No hay visita activa para este DNI", ex.getMessage());
    }

    @Test
    void exportVisitHistory_Success() {
        Visit closedVisit = new Visit();
        closedVisit.setVisitor(testVisitor);
        closedVisit.setSector(Sector.Administración);
        closedVisit.setQrToken(UUID.randomUUID().toString());
        closedVisit.setEntryTime(LocalDateTime.now().minusHours(2));
        closedVisit.setExitTime(LocalDateTime.now().minusHours(1));

        when(visitRepository.findAll()).thenReturn(List.of(testVisit, closedVisit));

        byte[] excelBytes = visitService.exportVisitHistory();
        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);
    }
}
