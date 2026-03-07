package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.Baby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface BabyRepository extends JpaRepository<Baby,Long> {
  Optional<Baby> findByName(String name);
  Optional<Baby> findByAgeInMonths(Integer ageInMonths);
  Optional<Baby> findByWeightInKilograms(Double weight);
  Optional<Baby> findByIsBioVulnerable(Boolean isBioVulnerable);
  Optional<Baby> findByGender(String gender);
  Optional<Baby> findByUserId(Long userId);
  Collection<Baby> findAllByUserId(Long userId);
  Optional<Baby> findByUserIdAndIsSelectedTrue(Long userId);
}
