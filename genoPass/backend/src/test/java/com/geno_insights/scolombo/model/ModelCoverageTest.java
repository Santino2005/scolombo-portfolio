package com.geno_insights.scolombo.model;

import com.geno_insights.scolombo.guard.model.entity.Guard;
import com.geno_insights.scolombo.visit.model.entity.Visit;
import com.geno_insights.scolombo.visitor.model.dto.CreateVisitorDto;
import com.geno_insights.scolombo.visitor.model.entity.Sector;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ModelCoverageTest {

    @Test
    void guardEntityTest() {
        Guard guard = new Guard();
        guard.setUserName("guardUser");
        guard.setHashedPin("hash123");

        assertEquals("guardUser", guard.getUserName());
        assertEquals("hash123", guard.getHashedPin());
        assertNull(guard.getId());
    }

    @Test
    void visitorEntityTest() {
        Visitor v1 = new Visitor();
        v1.setDni("12345678");
        v1.setFullName("Test Name");
        v1.setCompany("Geno");
        v1.setSector(Sector.Administración);
        v1.setPhotoUrl("http://photo.com/1.jpg");

        assertEquals("12345678", v1.getDni());
        assertEquals("Test Name", v1.getFullName());
        assertEquals("Geno", v1.getCompany());
        assertEquals(Sector.Administración, v1.getSector());
        assertEquals("http://photo.com/1.jpg", v1.getPhotoUrl());
        assertNull(v1.getId());

        Visitor v2 = new Visitor("87654321", "Name 2", "Co 2", Sector.Seguridad, "http://photo.com/2.jpg");
        assertEquals("87654321", v2.getDni());
        assertEquals("Name 2", v2.getFullName());
    }

    @Test
    void visitEntityTest() {
        Visitor visitor = new Visitor("111", "Visitor", "Co", Sector.Mantenimiento, "url");
        Visit visit = new Visit();
        LocalDateTime now = LocalDateTime.now();

        visit.setVisitor(visitor);
        visit.setSector(Sector.Mantenimiento);
        visit.setQrToken("token-xyz");
        visit.setEntryTime(now);
        visit.setExitTime(now.plusHours(1));

        assertEquals(visitor, visit.getVisitor());
        assertEquals(Sector.Mantenimiento, visit.getSector());
        assertEquals("token-xyz", visit.getQrToken());
        assertEquals(now, visit.getEntryTime());
        assertEquals(now.plusHours(1), visit.getExitTime());
        assertNull(visit.getId());
    }

    @Test
    void sectorEnumTest() {
        for (Sector s : Sector.values()) {
            assertNotNull(Sector.valueOf(s.name()));
        }
    }

    @Test
    void createVisitorDtoTest() {
        MockMultipartFile photo = new MockMultipartFile("photo", "pic.jpg", "image/jpeg", "data".getBytes());
        CreateVisitorDto dto = new CreateVisitorDto("123", "Full Name", "Company", Sector.Recepción, photo);

        assertEquals("123", dto.dni());
        assertEquals("Full Name", dto.fullName());
        assertEquals("Company", dto.company());
        assertEquals(Sector.Recepción, dto.sector());
        assertEquals(photo, dto.photo());
    }
}
