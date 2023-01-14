package com.gym.record.dto;

import com.gym.record.Record;
import com.gym.record.photo.RecordPhoto;
import com.gym.tag.Tag;
import com.gym.user.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecordGetRes {

    public RecordGetRes(Record record, User user, List<RecordPhoto> recordPhotos, List<Tag> tags) {
        this.content = record.getContent();
        this.createdAt = record.getCreatedAt();
        this.userName = user.getNickName();
        this.recordPhotos = recordPhotos;
        this.tags = tags;
    }
    private String content;
    private LocalDateTime createdAt;
    private String userName;
    private List<RecordPhoto> recordPhotos;
    private List<Tag> tags;
}
