package com.backendChallenge.email;

import com.backendChallenge.user.dto.UserStatsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {

    @Query("SELECT COUNT(e) FROM Email e WHERE e.userId = :userId AND e.emailDate = :date")
    long countByUserIdAndDate(@Param("userId") String userId, @Param("date") LocalDate date);

    @Query("""
       SELECT new com.backendChallenge.user.dto.UserStatsDTO(e.userId, COUNT(e))
       FROM Email e
       WHERE e.emailDate = :date
       GROUP BY e.userId
       """)
    List<UserStatsDTO> findDailyStats(@Param("date") LocalDate date);

}
