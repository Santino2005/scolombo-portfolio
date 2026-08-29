package com.uberclocked.api.emailData;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Test;

class AdminConfigTest {

  @Test
  void getAdminEmails_splitsAndTrimsCommaSeparatedEmails() throws Exception {
    AdminConfig adminConfig = new AdminConfig();
    Field field = AdminConfig.class.getDeclaredField("adminEmailsRaw");
    field.setAccessible(true);
    field.set(adminConfig, " admin1@mail.com , admin2@mail.com ");

    List<String> emails = adminConfig.getAdminEmails();

    assertEquals(2, emails.size());
    assertEquals("admin1@mail.com", emails.get(0));
    assertEquals("admin2@mail.com", emails.get(1));
  }
}
