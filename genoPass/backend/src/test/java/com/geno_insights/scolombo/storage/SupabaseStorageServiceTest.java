package com.geno_insights.scolombo.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class SupabaseStorageServiceTest {

    private SupabaseStorageService storageService;

    @BeforeEach
    void setUp() {
        storageService = new SupabaseStorageService();
        ReflectionTestUtils.setField(storageService, "supabaseUrl", "https://mock.supabase.co");
        ReflectionTestUtils.setField(storageService, "serviceKey", "test-key");
        ReflectionTestUtils.setField(storageService, "bucket", "visitors");
    }

    @Test
    void uploadVisitorPhoto_NullPhoto_ThrowsException() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                storageService.uploadVisitorPhoto(null)
        );
        assertEquals("Photo is required", ex.getMessage());
    }

    @Test
    void uploadVisitorPhoto_EmptyPhoto_ThrowsException() {
        MockMultipartFile empty = new MockMultipartFile("photo", "test.jpg", "image/jpeg", new byte[0]);
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                storageService.uploadVisitorPhoto(empty)
        );
        assertEquals("Photo is required", ex.getMessage());
    }

    @Test
    void uploadVisitorPhoto_NonImageContentType_ThrowsException() {
        MockMultipartFile textFile = new MockMultipartFile("photo", "test.txt", "text/plain", "hello".getBytes());
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                storageService.uploadVisitorPhoto(textFile)
        );
        assertEquals("File must be an image", ex.getMessage());
    }
}
