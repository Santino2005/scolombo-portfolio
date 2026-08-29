package com.geno_insights.scolombo.guard.controller;

import com.geno_insights.scolombo.guard.service.GuardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GuardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GuardService guardService;

    @InjectMocks
    private GuardController guardController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(guardController).build();
    }

    @Test
    void login_Success() throws Exception {
        when(guardService.login("admin", "1234")).thenReturn("Login successful");

        mockMvc.perform(post("/guard/login")
                        .param("username", "admin")
                        .param("pin", "1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }
}
