package com.gym.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Integer> {

    @Query("select r from Record r where r.user.userId = :userId and dAte_format(r.createdAt, '%Y-%m-%d') = :date")
    Record findAllByUserId(@Param("userId") Integer userId, @Param("date") String date);
}
