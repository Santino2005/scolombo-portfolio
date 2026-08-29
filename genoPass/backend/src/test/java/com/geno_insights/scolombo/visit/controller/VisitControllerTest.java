package com.geno_insights.scolombo.visit.controller;

import com.geno_insights.scolombo.visit.model.entity.Visit;
import com.geno_insights.scolombo.visit.service.VisitService;
import com.geno_insights.scolombo.visitor.model.entity.Sector;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VisitService visitService;

    @InjectMocks
    private VisitController visitController;

    private Visit testVisit;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();

        Visitor visitor = new Visitor("40123456", "Juan Perez", "Tech Corp", Sector.Operaciones, "https://photo.url/1.jpg");
        testVisit = new Visit();
        testVisit.setVisitor(visitor);
        testVisit.setSector(Sector.Operaciones);
        testVisit.setQrToken(UUID.randomUUID().toString());
        testVisit.setEntryTime(LocalDateTime.now());
    }

    @Test
    void registerEntry_Success() throws Exception {
        when(visitService.registerEntry(eq("40123456"), any(Sector.class))).thenReturn(testVisit);

        mockMvc.perform(post("/visit")
                        .param("dni", "40123456")
                        .param("sector", "Operaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qrToken").value(testVisit.getQrToken()));
    }

    @Test
    void registerExit_Success() throws Exception {
        testVisit.setExitTime(LocalDateTime.now());
        when(visitService.registerExit("test-token")).thenReturn(testVisit);

        mockMvc.perform(put("/visit/exit/test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qrToken").value(testVisit.getQrToken()));
    }

    @Test
    void getCredential_Success() throws Exception {
        when(visitService.getCredential("test-token")).thenReturn(testVisit);

        mockMvc.perform(get("/visit/credential/test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qrToken").value(testVisit.getQrToken()));
    }

    @Test
    void getActiveCredentialByDni_Success() throws Exception {
        when(visitService.getActiveCredentialByDni("40123456")).thenReturn(testVisit);

        mockMvc.perform(get("/visit/credential/active/40123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitor.dni").value("40123456"));
    }

    @Test
    void getTodayVisits_Success() throws Exception {
        when(visitService.getTodayVisits()).thenReturn(List.of(testVisit));

        mockMvc.perform(get("/visit/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getHistory_Success() throws Exception {
        when(visitService.getHistory()).thenReturn(List.of(testVisit));

        mockMvc.perform(get("/visit/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void exportVisitHistory_Success() throws Exception {
        byte[] fakeExcel = "FakeExcelContent".getBytes();
        when(visitService.exportVisitHistory()).thenReturn(fakeExcel);

        mockMvc.perform(get("/visit/history/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=visit-history.xlsx"))
                .andExpect(content().bytes(fakeExcel));
    }
}
