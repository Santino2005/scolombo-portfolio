package com.uberclocked.api.company.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uberclocked.api.company.mapper.CompanyMapper;
import com.uberclocked.api.company.model.dto.CompanyDataDto;
import com.uberclocked.api.company.model.entity.Company;
import com.uberclocked.api.company.service.CompanyService;
import com.uberclocked.api.security.TestSecurityConfig;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CompanyController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CompanyControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private CompanyService companyService;
  @MockitoBean private CompanyMapper mapper;

  @Test
  void createCompany_returns201() throws Exception {
    CompanyDataDto dto =
        new CompanyDataDto(UUID.randomUUID(), "Company A", "20-12345678-9", "comp@a.com", "123456");
    Company company = new Company();

    when(companyService.createCompany(any(Company.class))).thenReturn(company);
    when(mapper.toDto(company)).thenReturn(dto);

    mockMvc
        .perform(
            post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Company A"));
  }

  @Test
  void getAllCompanies_returns200() throws Exception {
    CompanyDataDto dto =
        new CompanyDataDto(UUID.randomUUID(), "Company A", "20-12345678-9", "comp@a.com", "123456");
    Company company = new Company();

    when(companyService.getAllCompanies()).thenReturn(List.of(company));
    when(mapper.toDto(company)).thenReturn(dto);

    mockMvc
        .perform(get("/companies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Company A"));
  }

  @Test
  void getCompany_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    CompanyDataDto dto =
        new CompanyDataDto(id, "Company A", "20-12345678-9", "comp@a.com", "123456");
    Company company = new Company();

    when(companyService.getCompany(id)).thenReturn(company);
    when(mapper.toDto(company)).thenReturn(dto);

    mockMvc
        .perform(get("/companies/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Company A"));
  }

  @Test
  void updateCompany_returns200() throws Exception {
    UUID id = UUID.randomUUID();
    CompanyDataDto dto =
        new CompanyDataDto(id, "Company A", "20-12345678-9", "comp@a.com", "123456");
    Company company = new Company();

    when(companyService.updateCompany(eq(id), any(CompanyDataDto.class), eq(mapper)))
        .thenReturn(company);
    when(mapper.toDto(company)).thenReturn(dto);

    mockMvc
        .perform(
            patch("/companies/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Company A"));
  }

  @Test
  void deleteCompany_returns204() throws Exception {
    UUID id = UUID.randomUUID();
    doNothing().when(companyService).deleteCompany(id);

    mockMvc.perform(delete("/companies/" + id)).andExpect(status().isNoContent());

    verify(companyService).deleteCompany(id);
  }
}
