package com.gym.tag;

import com.gym.record.photo.RecordPhoto;
import com.gym.tag.dto.TagGetRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.gym.record.Record;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("select t from Tag t join fetch t.record where t.record.recordId = :recordId")
    List<Tag> findAllByRecord(@Param("recordId") Integer recordId);

    @Query("select distinct t.name from Tag t where t.user.userId = :userId")
    Page<String> findByRecord(@Param("userId") Integer userId, Pageable pageable);

    Integer deleteAllByRecord(Record record);
}
