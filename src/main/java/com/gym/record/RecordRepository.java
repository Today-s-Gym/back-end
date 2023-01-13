package com.gym.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecordRepository extends JpaRepository<Record, Integer> {

   @Query("select r from Record r where r.user.userId =: userId and date_format(r.createdAt, '%Y-%m-%d') =: date")
   Record findRecordMonth(@Param("date") String date, @Param("userId") int userId);
}
