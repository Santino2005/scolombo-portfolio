package com.geno_insights.scolombo.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String serviceKey;

    @Value("${supabase.bucket}")
    private String bucket;

    private final RestClient restClient = RestClient.create();

    public String uploadVisitorPhoto(MultipartFile photo) {
        validatePhoto(photo);

        String fileName = UUID.randomUUID() + "-" + photo.getOriginalFilename();
        String path = "visitors/" + fileName;
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + path;

        try {
            restClient.post()
                    .uri(uploadUrl)
                    .header("apikey", serviceKey)
                    .header("Authorization", "Bearer " + serviceKey)
                    .contentType(MediaType.parseMediaType(Objects.requireNonNull(photo.getContentType())))
                    .body(photo.getBytes())
                    .retrieve()
                    .toBodilessEntity();

            return supabaseUrl + "/storage/v1/object/public+/" + bucket + "/" + path;

        } catch (IOException exception) {
            throw new RuntimeException("Could not upload visitor photo", exception);
        }
    }

    private void validatePhoto(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            throw new RuntimeException("Photo is required");
        }

        if (photo.getContentType() == null || !photo.getContentType().startsWith("image/")) {
            throw new RuntimeException("File must be an image");
        }
    }
}