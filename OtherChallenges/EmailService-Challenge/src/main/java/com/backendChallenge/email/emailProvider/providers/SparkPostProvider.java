package com.backendChallenge.email.emailProvider.providers;

import com.backendChallenge.email.emailProvider.EmailProviders;
import com.backendChallenge.result.EmailResult;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class SparkPostProvider implements EmailProviders {

    private static final String API_URL = "https://api.sparkpost.com/api/v1/transmissions";
    private final String apiKey;
    private static final Logger log = LoggerFactory.getLogger(SparkPostProvider.class);

    public SparkPostProvider(@Value("${sparkpost.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public EmailResult sendEmail(String from, String to, String subject, String body) {
        HttpURLConnection conn = null;
        try {
            log.info("From: {}", from);
            log.info("To: {}", to);
            log.info("Subject: {}", subject);

            conn = createConnection();
            String json = generateJson(from, to, subject, body);
            log.debug("JSON sent to SparkPost: {}", json);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            String responseBody = readResponse(conn, responseCode);
            String message = "SparkPost response code: " + responseCode;

            if (responseCode >= 400) {
                return EmailResult.notSuccess("SparkPost failed (" + responseCode + "): " + responseBody);
            }

            log.info("Email sent successfully via SparkPost to {}", to);
            return EmailResult.success(generateMail(from, to, subject, body), message, "SparkPost");

        } catch (Exception e) {
            log.error("SparkPost error sending mail: {}", e.getMessage());
            return EmailResult.notSuccess("SparkPost error: " + e.getMessage());
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private Mail generateMail(String from, String to, String subject, String body) {
        Email emailSender = new Email(from);
        Email emailReceiver = new Email(to);
        Content content = new Content("text/plain", body);
        return new Mail(emailSender, subject, emailReceiver, content);
    }

    private HttpURLConnection createConnection() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        return conn;
    }

    private String generateJson(String from, String to, String subject, String body) {
        return """
        {
          "options": { "sandbox": false },
          "content": {
            "from": { "email": "%s" },
            "subject": "%s",
            "text": "%s"
          },
          "recipients": [
            { "address": { "email": "%s" } }
          ]
        }
        """.formatted(from, subject, body, to);
    }

    private String readResponse(HttpURLConnection conn, int responseCode) {
        try (InputStream is = responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            log.warn("Can't read SparkPost response: {}", e.getMessage());
            return "<No body>";
        }
    }
}