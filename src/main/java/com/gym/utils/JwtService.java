package com.gym.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static Integer getUserId() {
        Integer userId = 1;
        return userId;
    }
}
