package com.geno_insights.scolombo.visitor.repository;

import com.geno_insights.scolombo.visitor.model.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
    Optional<Visitor> findByDni(String dni);
}
