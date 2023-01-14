package com.gym.record.photo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordPhotoRepository extends JpaRepository<RecordPhoto, Integer> {

    List<RecordPhoto> findAllByRecord(int recordId);
}
