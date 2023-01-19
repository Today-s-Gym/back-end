package com.gym.record.dto;

import com.gym.record.Record;
import com.gym.record.photo.RecordPhoto;
import com.gym.record.photo.RecordPhotoGetRes;
import com.gym.tag.Tag;
import com.gym.tag.dto.TagGetRes;
import com.gym.user.User;
import com.gym.utils.UtilService;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecordGetRes {
    public RecordGetRes(Record record, User user) {
        this.content = record.getContent();
        this.createdAt = record.getCreatedAt();
        this.userName = user.getNickName();
        if(record.getPhotoList().isEmpty()){
            this.recordPhotos = new ArrayList<>();
            this.recordPhotos.add(new RecordPhotoGetRes(UtilService.returnRecordBaseImage()));
        }
        else {
            this.recordPhotos = record.getPhotoList().stream()
                    .map(recordPhoto -> new RecordPhotoGetRes(recordPhoto))
                    .collect(Collectors.toList());
        }
        this.tags = record.getTagList().stream()
                .map(tag -> new TagGetRes(tag))
                .collect(Collectors.toList());
    }
    private String content;
    private LocalDateTime createdAt;
    private String userName;
    private List<RecordPhotoGetRes> recordPhotos;
    private List<TagGetRes> tags;
}
