package com.gym.login.dto;

import com.gym.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersaveRequestDTO {

    private String nickName;
    private String introduce;
    private String email;
    private String refreshToken;

    @Builder
    public UsersaveRequestDTO(String nickName, String introduce, String email, String refreshToken){
        this.nickName = nickName;
        this.introduce = introduce;
        this.email = email;
        this.refreshToken = refreshToken;
    }

    //request dto 로 받은 Posts 객체를 entity 화하여 저장하는 용도
    public User toEntity(){
        return User.builder().nickName(nickName).introduce(introduce).email(email).refreshToken(refreshToken).build();
    }


}
