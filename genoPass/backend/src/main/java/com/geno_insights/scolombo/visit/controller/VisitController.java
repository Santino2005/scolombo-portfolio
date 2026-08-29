package com.geno_insights.scolombo.visit.controller;

import com.geno_insights.scolombo.visit.model.entity.Visit;
import com.geno_insights.scolombo.visit.service.VisitService;
import com.geno_insights.scolombo.visitor.model.entity.Sector;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;
    private final Logger logger = LoggerFactory.getLogger(VisitController.class);

    @PostMapping
    public Visit registerEntry(
            @RequestParam String dni,
            @RequestParam Sector sector
    ) {
        return visitService.registerEntry(dni, sector);
    }
    @PutMapping("/exit/{qrToken}")
    public Visit registerExit(@PathVariable String qrToken) {
        return visitService.registerExit(qrToken);
    }

    @GetMapping("/credential/{qrToken}")
    public Visit getCredential(@PathVariable String qrToken) {
        return visitService.getCredential(qrToken);
    }
    @GetMapping("/credential/active/{dni}")
    public Visit getActiveCredentialByDni(@PathVariable String dni) {
        Visit visit = visitService.getActiveCredentialByDni(dni);
        logger.info("Visit retrieved: {}", visit.getVisitor().getPhotoUrl());
        return visit;
    }

    @GetMapping("/today")
    public List<Visit> getTodayVisits() {
        return visitService.getTodayVisits();
    }

    @GetMapping("/history")
    public List<Visit> getHistory() {
        return visitService.getHistory();
    }

    @GetMapping("/history/export")
    public ResponseEntity<byte[]> exportVisitHistory() {
        byte[] file = visitService.exportVisitHistory();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=visit-history.xlsx"
                )
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(file);
    }

}
