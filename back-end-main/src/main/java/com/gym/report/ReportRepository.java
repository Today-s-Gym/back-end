package com.gym.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("select r from Report r where r.reporterId =:reporterId and r.reportedId =:reportedId and r.type = 'USER'")
    Optional<Report> findUserReport(@Param("reporterId") Integer reporterId, @Param("reportedId") Integer reportedId);

    @Query("select r from Report r where r.reporterId =:reporterId and r.reportedId =:reportedId and r.type = 'POST'")
    Optional<Report> findPostReport(@Param("reporterId") Integer reporterId, @Param("reportedId") Integer reportedId);

    @Query("select r from Report r where r.reporterId =:reporterId and r.reportedId =:reportedId and r.type = 'COMMENT'")
    Optional<Report> findCommentReport(@Param("reporterId") Integer reporterId, @Param("reportedId") Integer reportedId);
}