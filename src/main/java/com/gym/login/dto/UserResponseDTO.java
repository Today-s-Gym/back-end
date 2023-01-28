package com.gym.login.dto;

import com.gym.user.User;
import lombok.Getter;

@Getter
public class UserResponseDTO {
    private Integer userId;

    private String nickName;
    private String introduce;
    private String email;
    private String refreshToken;


    //repository 를 통해 조회한 entity 를 dto 로 변환 용도
    public UserResponseDTO(User user){
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.refreshToken = user.getRefreshToken();
    }
}
