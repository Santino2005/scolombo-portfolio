package com.geno_insights.scolombo.visitor.model.dto;

import com.geno_insights.scolombo.visitor.model.entity.Sector;
import org.springframework.web.multipart.MultipartFile;

public record CreateVisitorDto(String dni, String fullName, String company, Sector sector, MultipartFile photo) {
}
