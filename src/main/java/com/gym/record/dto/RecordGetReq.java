package com.gym.record.dto;

import com.gym.record.photo.RecordPhoto;
import com.gym.tag.Tag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecordGetReq {
    private String content;
    private List<RecordPhoto> recordPhotos = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
}
