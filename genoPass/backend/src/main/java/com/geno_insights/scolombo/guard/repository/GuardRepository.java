package com.geno_insights.scolombo.guard.repository;

import com.geno_insights.scolombo.guard.model.entity.Guard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuardRepository extends JpaRepository<Guard, UUID> {

    Optional<Guard> findByUserName(String username);
}