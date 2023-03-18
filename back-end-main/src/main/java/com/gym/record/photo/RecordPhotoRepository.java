package com.gym.record.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.gym.record.Record;

import java.util.List;

public interface RecordPhotoRepository extends JpaRepository<RecordPhoto, Integer> {

    @Query("select r from RecordPhoto r where r.record.recordId = :recordId")
    List<RecordPhoto> findAllByRecord(@Param("recordId") Integer recordId);

    @Query("select rp.id from RecordPhoto rp where rp.record.recordId = :recordId")
    List<Integer> findAllId(@Param("recordId") Integer recordId);


    @Modifying
    @Query("delete from RecordPhoto rp where rp.id in :ids")
    Integer deleteAllByRecord(@Param("ids") List<Integer> ids);
}
