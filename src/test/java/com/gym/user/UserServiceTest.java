package com.gym.user;

import com.gym.tag.config.exception.BaseException;
import com.gym.user.dto.AccountPrivacyReq;
import com.gym.utils.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
class UserServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @Transactional
    void changeAccountPrivacy(boolean locked) {
        // given
        AccountPrivacyReq accountPrivacyReq = new AccountPrivacyReq(locked);

        // when
        Integer userId = userService.changeAccountPrivacy(JwtService.getUserId(), accountPrivacyReq.isLocked());

        // then
        User findUser = userRepository.findById(userId).get();
        Assertions.assertThat(findUser.isLocked()).isEqualTo(locked);
    }

    @Test
    @DisplayName("사용자 이메일 조회 테스트")
    void findUserEmailByUserId() throws BaseException {
        // given
        Integer userId = 2;

        // when
        String email = userService.findUserEmailByUserId(userId);

        // then
        Assertions.assertThat(email).isEqualTo("user2@gmail.com");
    }
}