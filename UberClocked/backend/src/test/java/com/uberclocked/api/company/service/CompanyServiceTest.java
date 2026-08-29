package com.uberclocked.api.company.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.common.exceptions.ResourceDoesNotExistsException;
import com.uberclocked.api.company.mapper.CompanyMapper;
import com.uberclocked.api.company.model.dto.CompanyDataDto;
import com.uberclocked.api.company.model.entity.Company;
import com.uberclocked.api.company.repository.CompanyRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

  @Mock private CompanyRepository companyRepository;

  private CompanyService companyService;

  @BeforeEach
  void setUp() {
    companyService = new CompanyService(companyRepository);
  }

  @Test
  void createCompany_whenDomainNotExists_createsAndReturns() {
    Company company = new Company();
    company.setName("Tech Corp");
    company.setEmailDomain("techcorp.com");

    when(companyRepository.existsByEmailDomain("techcorp.com")).thenReturn(false);
    when(companyRepository.save(any(Company.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Company result = companyService.createCompany(company);

    assertNotNull(result.getId());
    assertEquals("Tech Corp", result.getName());
    verify(companyRepository).save(company);
  }

  @Test
  void createCompany_whenDomainExists_throwsIllegalStateException() {
    Company company = new Company();
    company.setEmailDomain("existing.com");

    when(companyRepository.existsByEmailDomain("existing.com")).thenReturn(true);

    assertThrows(IllegalStateException.class, () -> companyService.createCompany(company));
  }

  @Test
  void getAllCompanies_returnsList() {
    Company company = new Company();
    when(companyRepository.findAll()).thenReturn(List.of(company));

    List<Company> list = companyService.getAllCompanies();

    assertEquals(1, list.size());
  }

  @Test
  void getCompany_whenFound_returnsCompany() {
    UUID id = UUID.randomUUID();
    Company company = new Company();
    company.setId(id);

    when(companyRepository.findById(id)).thenReturn(Optional.of(company));

    Company result = companyService.getCompany(id);

    assertEquals(id, result.getId());
  }

  @Test
  void getCompany_whenNotFound_throwsResourceDoesNotExistsException() {
    UUID id = UUID.randomUUID();
    when(companyRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(ResourceDoesNotExistsException.class, () -> companyService.getCompany(id));
  }

  @Test
  void updateCompany_updatesAndSaves() {
    UUID id = UUID.randomUUID();
    Company company = new Company();
    company.setId(id);
    CompanyDataDto dto =
        new CompanyDataDto(id, "Updated Name", "20-12345678-9", "updated@comp.com", "123456");
    CompanyMapper mapper = mock(CompanyMapper.class);

    when(companyRepository.findById(id)).thenReturn(Optional.of(company));
    when(companyRepository.save(company)).thenReturn(company);

    Company result = companyService.updateCompany(id, dto, mapper);

    verify(mapper).update(dto, company);
    verify(companyRepository).save(company);
    assertEquals(company, result);
  }

  @Test
  void deleteCompany_deletesEntity() {
    UUID id = UUID.randomUUID();
    Company company = new Company();
    company.setId(id);

    when(companyRepository.findById(id)).thenReturn(Optional.of(company));

    companyService.deleteCompany(id);

    verify(companyRepository).delete(company);
  }

  @Test
  void findByDomain_returnsOptional() {
    Company company = new Company();
    when(companyRepository.findByEmailDomain("domain.com")).thenReturn(Optional.of(company));

    Optional<Company> result = companyService.findByDomain("domain.com");

    assertTrue(result.isPresent());
    assertEquals(company, result.get());
  }
}
