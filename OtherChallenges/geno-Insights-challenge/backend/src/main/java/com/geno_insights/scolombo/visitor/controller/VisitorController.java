package com.geno_insights.scolombo.visitor.controller;

import com.geno_insights.scolombo.visitor.model.dto.CreateVisitorDto;
import com.geno_insights.scolombo.visitor.model.entity.Sector;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import com.geno_insights.scolombo.visitor.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/visitor")
@RequiredArgsConstructor
public class VisitorController {
    private final VisitorService visitorService;

    @GetMapping("/{dni}")
    public Visitor findByDni(@PathVariable String dni) {
        return visitorService.findByDni(dni);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Visitor registerVisitor(
            @RequestParam String dni,
            @RequestParam String fullName,
            @RequestParam String company,
            @RequestParam Sector sector,
            @RequestPart MultipartFile photo
    ) {

        CreateVisitorDto dto = new CreateVisitorDto(
                dni,
                fullName,
                company,
                sector,
                photo
        );

        return visitorService.registerVisitor(dto);
    }

    @GetMapping("/count")
    public long countVisitors() {
        return visitorService.countVisitors();
    }

}
