package com.uberclocked.api.company.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.uberclocked.api.company.model.dto.CompanyDataDto;
import com.uberclocked.api.company.model.entity.Company;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CompanyMapperTest {

  private final CompanyMapper mapper =
      new CompanyMapper() {
        @Override
        public CompanyDataDto toDto(Company entity) {
          return null;
        }

        @Override
        public void update(CompanyDataDto dto, Company entity) {}
      };

  @Test
  void extractDomain_setsEmailDomain() {
    CompanyDataDto dto =
        new CompanyDataDto(
            UUID.randomUUID(), "Company", "20-12345678-9", "  Admin@MyCompany.COM ", "123");
    Company company = new Company();

    mapper.extractDomain(dto, company);

    assertEquals("admin@mycompany.com", company.getEmailDomain());
  }
}
