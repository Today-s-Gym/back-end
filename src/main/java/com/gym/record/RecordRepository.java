package com.gym.record;

import com.gym.record.dto.RecordGetRecentRes;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Integer> {

    Optional<Record> getByRecordId(Integer recordId);

    @Query("select r from Record r where r.user.userId = :userId and date_format(r.createdAt, '%Y-%m-%d') = :date")
    Record findAllByDay(@Param("userId") Integer userId, @Param("date") String date);

    @Query("select r from Record r where r.user.userId = :userId and date_format(r.createdAt, '%Y-%m') = :month")
    List<Record> findAllByMonth(@Param("userId") Integer userId, @Param("month") String month);

    @Query("select count(r) from Record r where date_format(r.createdAt, '%Y-%m-%d') = :date and r.user.userId = :userId")
    Integer findByRecordDate(@Param("date") String date, @Param("userId") Integer userId);


    @Modifying
    @Query("delete from Record r where r.recordId = :recordId")
    Integer deleteAllByRecordId(@Param("recordId") Integer recordId);

    @Query("select r from Record r where r.user.userId = :userId")
    Page<Record> findAllByUserId(@Param("userId") Integer userId, PageRequest pageRequest);

    @Query("select count(r) from Record r where r.user.userId = :userId")
    Integer countByUserId(@Param("userId") Integer userId);

    @Query("select count(r) from Record r where r.user.userId = :userId and date_format(r.createdAt, '%Y-%m') = :month")
    Integer countByUserIdMonth(@Param("userId") Integer userId, @Param("month") String month);

}
