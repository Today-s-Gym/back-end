package com.gym.record.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordPhotoRepository extends JpaRepository<RecordPhoto, Integer> {

    @Query("select r from RecordPhoto r where r.record.recordId = :recordId")
    List<RecordPhoto> findAllByRecord(@Param("recordId") Integer recordId);
}
