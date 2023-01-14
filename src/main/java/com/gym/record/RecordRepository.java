package com.gym.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface RecordRepository extends JpaRepository<Record, Integer> {

   @Query("select r from Record r where date_format(r.createdAt, '%Y-%m-%d') = :date and r.user.userId = :userId")
   Record findRecordDay(@Param("date") String date, @Param("userId") Integer userId);

}
