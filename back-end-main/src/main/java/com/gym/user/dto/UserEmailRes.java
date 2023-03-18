package com.gym.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserEmailRes {
    private String email;

    public UserEmailRes(String email) {
        this.email = email;
    }
}
