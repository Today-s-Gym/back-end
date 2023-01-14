package com.gym.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gym.record.photo.RecordPhoto;
import com.gym.tag.Tag;
import lombok.Data;

import java.util.List;

@Data
public class RecordRequestDto {
    private String content;
    private List<RecordPhoto> recordPhotos;
    private List<Tag> tags;
}
