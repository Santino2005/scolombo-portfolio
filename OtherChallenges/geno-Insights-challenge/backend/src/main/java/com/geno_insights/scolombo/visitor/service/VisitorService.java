package com.geno_insights.scolombo.visitor.service;

import com.geno_insights.scolombo.storage.SupabaseStorageService;
import com.geno_insights.scolombo.visitor.model.dto.CreateVisitorDto;
import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import com.geno_insights.scolombo.visitor.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final SupabaseStorageService storageService;

    public Visitor findByDni(String dni) {
        return visitorRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));
    }

    public Visitor registerVisitor(CreateVisitorDto visitor) {
        String photoUrl = storageService.uploadVisitorPhoto(visitor.photo());
        Visitor visitorToRegister = new Visitor(visitor.dni(), visitor.fullName(), visitor.company(), visitor.sector(),photoUrl);
        return visitorRepository.save(visitorToRegister);
    }
    public long countVisitors() {
        return visitorRepository.count();
    }
}
