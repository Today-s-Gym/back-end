package com.gym.record.dto;

import com.gym.record.photo.RecordPhoto;
import com.gym.utils.UtilService;
import jdk.jshell.execution.Util;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecordGetRecentRes {

    private Integer recordId;
    private String content;
    private String createdTime;
    private String imgUrl;

    public RecordGetRecentRes(Integer recordId, String content, LocalDateTime createdAt, List<RecordPhoto> recordPhotos) {
        this.recordId = recordId;
        this.content = content;
        this.createdTime = UtilService.convertLocalDateTimeToLocalDate(createdAt);
        if (recordPhotos.isEmpty()) {
            this.imgUrl =  "";
        } else {
            this.imgUrl = recordPhotos.get(0).getImgUrl();
        }
    }
}
