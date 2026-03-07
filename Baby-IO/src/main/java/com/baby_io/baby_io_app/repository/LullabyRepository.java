package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.Lullaby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LullabyRepository extends JpaRepository<Lullaby, Long> {
  Optional<Lullaby> findBySongNumber(Integer songNumber);
  Optional<Lullaby> findByName(String name);
}
