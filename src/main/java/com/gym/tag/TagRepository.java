package com.gym.tag;

import com.gym.record.photo.RecordPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("select t from Tag t where t.record.recordId = :recordId")
    List<Tag> findAllByRecord(@Param("recordId") Integer recordId);
}
