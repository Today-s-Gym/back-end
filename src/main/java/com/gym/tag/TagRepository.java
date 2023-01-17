package com.gym.tag;

import com.gym.record.photo.RecordPhoto;
import com.gym.tag.dto.TagGetRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.gym.record.Record;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("select t.id from Tag t where t.record.recordId = :recordId")
    List<Integer> findAllId(@Param("recordId") Integer recordId);

    @Query("select distinct t.name from Tag t where t.user.userId = :userId")
    Page<String> findByRecord(@Param("userId") Integer userId, Pageable pageable);

    @Modifying
    @Query("delete from Tag t where t.id in :ids")
    Integer deleteAllByRecord(@Param("ids") List<Integer> ids);
}
