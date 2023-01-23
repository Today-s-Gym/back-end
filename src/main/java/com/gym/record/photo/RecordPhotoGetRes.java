package com.gym.record.photo;

import lombok.Data;

@Data
public class RecordPhotoGetRes {
    public RecordPhotoGetRes(RecordPhoto recordPhoto) {
        this.img_url = recordPhoto.getImgUrl();
    }
    public RecordPhotoGetRes(String img_Url){
        this.img_url = img_Url;
    }
    private String img_url;
}
