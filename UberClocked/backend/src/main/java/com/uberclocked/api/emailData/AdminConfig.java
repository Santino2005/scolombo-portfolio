package com.uberclocked.api.emailData;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminConfig {

  @Value("${app.admin.emails}")
  private String adminEmailsRaw;

  public List<String> getAdminEmails() {
    return Arrays.stream(adminEmailsRaw.split(",")).map(String::trim).toList();
  }
}
