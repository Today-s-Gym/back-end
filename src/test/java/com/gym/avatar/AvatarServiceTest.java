package com.gym.avatar;

import com.gym.avatar.avatar.AvatarStep;
import com.gym.user.User;
import com.gym.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AvatarServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired AvatarService avatarService;

    @Test
    void setInitialAvatar() {
        User user = new User();
        user.update("test_user", "안녕");
        userRepository.save(user);
        avatarService.setInitialAvatar(user);

        Assertions.assertThat(user.getMyAvatar().getAvatarStep()).isEqualTo(AvatarStep.getInitialAvatarStep());
    }
}