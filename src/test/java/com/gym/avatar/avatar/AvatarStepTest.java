package com.gym.avatar.avatar;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AvatarStepTest {
    @Autowired
    private MyAvatarRepository myAvatarRepository;

    @Test
    void getInitialAvatarStep() {
        AvatarStep result = AvatarStep.getInitialAvatarStep();
        Assertions.assertThat(result).isEqualTo(AvatarStep.AVATAR1_STEP1);

        MyAvatar my = myAvatarRepository.findByAvatarStep(result).get();
        Assertions.assertThat(my.getAvatarStep()).isEqualTo(AvatarStep.AVATAR1_STEP1);
    }
}