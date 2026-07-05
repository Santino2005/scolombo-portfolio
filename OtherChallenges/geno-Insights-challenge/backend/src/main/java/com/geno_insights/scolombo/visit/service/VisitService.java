package com.geno_insights.scolombo.visit.service;

import com.geno_insights.scolombo.guard.service.GuardService;
import com.geno_insights.scolombo.visit.model.entity.Visit;
import com.geno_insights.scolombo.visit.repository.VisitRepository;
import com.geno_insights.scolombo.visitor.model.entity.Sector;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import com.geno_insights.scolombo.visitor.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.apache.catalina.manager.StatusTransformer.formatTime;
import static org.apache.tomcat.util.http.FastHttpDateFormat.formatDate;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitorService visitorService;
    private static final Logger logger =
            LoggerFactory.getLogger(VisitService.class);

    public Visit registerEntry(
            String dni,
            Sector sector
    ) {

        Visitor visitor = visitorService.findByDni(dni);
        visitRepository.findByVisitorAndExitTimeIsNull(visitor)
                .ifPresent(activeVisit -> {
                    throw new RuntimeException("Visitor already inside");
                });
        Visit visit = new Visit();
        visit.setVisitor(visitor);
        visit.setSector(sector);
        visit.setQrToken(UUID.randomUUID().toString());
        visit.setEntryTime(LocalDateTime.now());

        return visitRepository.save(visit);
    }

    public Visit registerExit(String qrToken) {
        Visit visit = visitRepository.findByQrToken(qrToken)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        if (visit.getExitTime() != null) {
            throw new RuntimeException("Visit already closed");
        }

        visit.setExitTime(LocalDateTime.now());

        return visitRepository.save(visit);
    }

    public Visit getCredential(String qrToken) {
        return visitRepository.findByQrToken(qrToken)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
    }

    public List<Visit> getTodayVisits() {
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        return visitRepository.findByEntryTimeBetween(start, end);
    }

    public List<Visit> getHistory() {
        return visitRepository.findAll();
    }

    public Visit getActiveCredentialByDni(String dni) {
        return visitRepository.findByVisitorDniAndExitTimeIsNull(dni)
                .orElseThrow(() -> new RuntimeException("No hay visita activa para este DNI"));
    }

    public byte[] exportVisitHistory() {
        List<Visit> visits = visitRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Historial");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Fecha");
            header.createCell(1).setCellValue("Ingreso");
            header.createCell(2).setCellValue("Salida");
            header.createCell(3).setCellValue("DNI");
            header.createCell(4).setCellValue("Nombre");
            header.createCell(5).setCellValue("Empresa");
            header.createCell(6).setCellValue("Sector");
            header.createCell(7).setCellValue("Estado");

            int rowIndex = 1;

            for (Visit visit : visits) {
                Row row = sheet.createRow(rowIndex);

                row.createCell(0).setCellValue(formatDate(visit.getEntryTime()));
                row.createCell(1).setCellValue(formatTime(visit.getEntryTime()));
                row.createCell(2).setCellValue(formatTime(visit.getExitTime()));
                row.createCell(3).setCellValue(visit.getVisitor().getDni());
                row.createCell(4).setCellValue(visit.getVisitor().getFullName());
                row.createCell(5).setCellValue(visit.getVisitor().getCompany());
                row.createCell(6).setCellValue(visit.getSector().name());
                row.createCell(7).setCellValue(
                        visit.getExitTime() == null ? "Activo" : "Finalizado"
                );

                rowIndex++;
            }

            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException exception) {
            throw new RuntimeException("Could not export visit history", exception);
        }
    }
    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "-";
        }

        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "-";
        }

        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
