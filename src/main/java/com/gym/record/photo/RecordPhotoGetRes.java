package com.gym.record.photo;

import lombok.Data;

@Data
public class RecordPhotoGetRes {
    public RecordPhotoGetRes(RecordPhoto recordPhoto) {
        this.img_url = recordPhoto.getImgUrl();
    }
    private String img_url;
}
