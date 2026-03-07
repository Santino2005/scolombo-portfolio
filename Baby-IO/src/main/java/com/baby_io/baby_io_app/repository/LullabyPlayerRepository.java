package com.baby_io.baby_io_app.repository;

import com.baby_io.baby_io_app.entity.LullabyPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LullabyPlayerRepository extends JpaRepository<LullabyPlayer, Long> {
  Optional<LullabyPlayer> findByUserId(Long userId);
}

