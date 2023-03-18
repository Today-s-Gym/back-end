package com.gym.login.dto;

import com.gym.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDTO {
    private String email;

    @Builder
    public UserUpdateRequestDTO(String email){
        this.email = email;

    }

    public User toEntity(){
        return User.builder().email(email).build();
    }


}
