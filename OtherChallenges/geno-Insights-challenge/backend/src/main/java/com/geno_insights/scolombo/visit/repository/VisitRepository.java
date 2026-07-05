package com.geno_insights.scolombo.visit.repository;

import com.geno_insights.scolombo.visit.model.entity.Visit;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {
    Optional<Visit> findByQrToken(String qrToken);
    Optional<Visit> findByVisitorAndExitTimeIsNull(Visitor visitor);
    List<Visit> findByEntryTimeBetween(LocalDateTime start, LocalDateTime end);
    Optional<Visit> findByVisitorDniAndExitTimeIsNull(String dni);
}
