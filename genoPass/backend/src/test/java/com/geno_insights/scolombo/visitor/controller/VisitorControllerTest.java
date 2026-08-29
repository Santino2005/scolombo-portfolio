package com.geno_insights.scolombo.visitor.controller;

import com.geno_insights.scolombo.visitor.model.dto.CreateVisitorDto;
import com.geno_insights.scolombo.visitor.model.entity.Sector;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import com.geno_insights.scolombo.visitor.service.VisitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VisitorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VisitorService visitorService;

    @InjectMocks
    private VisitorController visitorController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(visitorController).build();
    }

    @Test
    void findByDni_Success() throws Exception {
        Visitor visitor = new Visitor("40123456", "Juan Perez", "Tech Corp", Sector.Operaciones, "https://photo.url/1.jpg");
        when(visitorService.findByDni("40123456")).thenReturn(visitor);

        mockMvc.perform(get("/visitor/40123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("40123456"))
                .andExpect(jsonPath("$.fullName").value("Juan Perez"));
    }

    @Test
    void registerVisitor_Success() throws Exception {
        MockMultipartFile photo = new MockMultipartFile("photo", "selfie.jpg", "image/jpeg", "content".getBytes());
        Visitor visitor = new Visitor("40123456", "Juan Perez", "Tech Corp", Sector.Operaciones, "https://photo.url/1.jpg");

        when(visitorService.registerVisitor(any(CreateVisitorDto.class))).thenReturn(visitor);

        mockMvc.perform(multipart("/visitor")
                        .file(photo)
                        .param("dni", "40123456")
                        .param("fullName", "Juan Perez")
                        .param("company", "Tech Corp")
                        .param("sector", "Operaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("40123456"))
                .andExpect(jsonPath("$.photoUrl").value("https://photo.url/1.jpg"));
    }

    @Test
    void countVisitors_Success() throws Exception {
        when(visitorService.countVisitors()).thenReturn(50L);

        mockMvc.perform(get("/visitor/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(50));
    }
}
