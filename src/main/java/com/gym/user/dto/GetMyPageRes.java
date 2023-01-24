package com.gym.user.dto;

import com.gym.avatar.avatar.MyAvatar;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMyPageRes {
    private String avatarImgUrl;
    private String nickname;
    private String categoryName;
    private String introduce;
    private UserRecordCount userRecordCount;
    private boolean locked;

    public GetMyPageRes(MyAvatar myAvatar, String nickname, String categoryName, String introduce, boolean locked) {
        this.avatarImgUrl = myAvatar.getAvatarStep().getImgUrl();
        this.nickname = nickname;
        this.categoryName = categoryName;
        this.introduce = introduce;
        this.locked = locked;
    }

    private GetMyPageRes(boolean locked) {
        this.locked = locked;
    }

    public static GetMyPageRes lockedMyPageInfo() {
        return new GetMyPageRes(true);
    }


    public void setAvatarImgUrl(String avatarImgUrl) {
        this.avatarImgUrl = avatarImgUrl;
    }

    public void setUserRecordCount(UserRecordCount userRecordCount) {
        this.userRecordCount = userRecordCount;
    }
}
