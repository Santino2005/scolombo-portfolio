package com.geno_insights.scolombo.visitor.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "visitors")
@Getter
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    @Setter
    private String dni;

    @Setter
    @Column(nullable = false)
    private String fullName;

    @Setter
    @Column(nullable = false)
    private String company;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sector sector;

    @Setter
    @Column(nullable = false)
    private String photoUrl;

    public Visitor(String dni, String fullName, String company, Sector sector,String photoUrl) {
        this.dni = dni;
        this.fullName = fullName;
        this.company = company;
        this.sector = sector;
        this.photoUrl = photoUrl;
    }

    public Visitor() {

    }
}