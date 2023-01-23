package com.gym.avatar.avatar.dto;

import com.gym.avatar.avatar.MyAvatar;
import lombok.Getter;

@Getter
public class MyAvatarDto {
    private int myAvatarId;
    private String avatarName;
    private int level;
    private String imgUrl;

    public MyAvatarDto(MyAvatar myAvatar) {
        this.myAvatarId = myAvatar.getMyAvatarId();
        this.avatarName = myAvatar.getAvatar().getName();
        this.level = myAvatar.getAvatarStep().getLevel();
        this.imgUrl = myAvatar.getAvatarStep().getImgUrl();
    }
}
