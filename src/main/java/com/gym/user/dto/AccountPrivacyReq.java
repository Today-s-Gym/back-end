package com.gym.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountPrivacyReq {
    private boolean locked;
}
