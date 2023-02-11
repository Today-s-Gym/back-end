package com.gym.user;

import com.gym.avatar.avatar.dto.MyAvatarDto;
import com.gym.config.exception.BaseException;
import com.gym.user.dto.GetMyPageRes;
import com.gym.login.jwt.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
class UserServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

