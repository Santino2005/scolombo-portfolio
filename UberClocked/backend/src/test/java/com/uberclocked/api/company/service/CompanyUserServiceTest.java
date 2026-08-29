package com.uberclocked.api.company.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.uberclocked.api.company.model.entity.Company;
import com.uberclocked.api.company.model.entity.CompanyUser;
import com.uberclocked.api.company.repository.CompanyUserRepository;
import com.uberclocked.api.user.model.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyUserServiceTest {

  @Mock private CompanyUserRepository companyUserRepository;

  private CompanyUserService companyUserService;

  @BeforeEach
  void setUp() {
    companyUserService = new CompanyUserService(companyUserRepository);
  }

  @Test
  void addUserToCompany_whenNotBelongs_savesAndReturns() {
    User user = new User();
    Company company = new Company();

    when(companyUserRepository.existsByUserAndCompany(user, company)).thenReturn(false);
    when(companyUserRepository.save(any(CompanyUser.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    CompanyUser result = companyUserService.addUserToCompany(user, company);

    assertNotNull(result);
    assertEquals(user, result.getUser());
    assertEquals(company, result.getCompany());
  }

  @Test
  void addUserToCompany_whenAlreadyBelongs_throwsIllegalStateException() {
    User user = new User();
    Company company = new Company();

    when(companyUserRepository.existsByUserAndCompany(user, company)).thenReturn(true);

    assertThrows(
        IllegalStateException.class, () -> companyUserService.addUserToCompany(user, company));
  }

  @Test
  void removeUserFromCompany_whenFound_deletes() {
    User user = new User();
    Company company = new Company();
    CompanyUser relation = new CompanyUser(user, company);

    when(companyUserRepository.findByUserAndCompany(user, company))
        .thenReturn(Optional.of(relation));

    companyUserService.removeUserFromCompany(user, company);

    verify(companyUserRepository).delete(relation);
  }

  @Test
  void isUserInCompany_returnsResult() {
    User user = new User();
    Company company = new Company();

    when(companyUserRepository.existsByUserAndCompany(user, company)).thenReturn(true);

    assertTrue(companyUserService.isUserInCompany(user, company));
  }

  @Test
  void getUsersOfCompany_returnsList() {
    Company company = new Company();
    CompanyUser relation = new CompanyUser(new User(), company);

    when(companyUserRepository.findByCompany(company)).thenReturn(List.of(relation));

    List<CompanyUser> list = companyUserService.getUsersOfCompany(company);

    assertEquals(1, list.size());
  }

  @Test
  void getCompaniesOfUser_returnsList() {
    User user = new User();
    CompanyUser relation = new CompanyUser(user, new Company());

    when(companyUserRepository.findByUser(user)).thenReturn(List.of(relation));

    List<CompanyUser> list = companyUserService.getCompaniesOfUser(user);

    assertEquals(1, list.size());
  }
}
