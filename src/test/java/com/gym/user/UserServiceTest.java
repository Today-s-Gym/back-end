package com.gym.user;

import com.gym.user.dto.AccountPrivacyReq;
import com.gym.utils.JwtService;
import org.assertj.core.api.Assertions;
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
}