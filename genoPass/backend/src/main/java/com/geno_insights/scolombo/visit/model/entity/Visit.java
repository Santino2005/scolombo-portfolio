package com.geno_insights.scolombo.visit.model.entity;

import com.geno_insights.scolombo.visitor.model.entity.Sector;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "visits")
@Getter
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sector sector;

    @Column(unique = true, nullable = false)
    @Setter
    private String qrToken;

    @Setter
    @Column(nullable = false)
    private LocalDateTime entryTime;

    @Setter
    private LocalDateTime exitTime;

    @ManyToOne(optional = false)
    @Setter
    private Visitor visitor;

}