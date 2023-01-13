package com.gym.record;

import com.gym.record.photo.RecordPhoto;
import com.gym.tag.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecordResponseDto {
    private int recordId;
    private String content;
    private LocalDateTime createdAt;
    private String userName;
    public RecordResponseDto(Record record) {
        this.recordId = record.getRecordId();
        this.content = record.getContent();
        this.createdAt = record.getCreatedAt();
        this.userName = record.getUser().getNickName();
    }
    private List<RecordPhoto> recordPhotos;
    private List<Tag> tags;
}
