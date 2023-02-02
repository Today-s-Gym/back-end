package com.gym.avatar;

import com.gym.avatar.avatar.AvatarStep;
import com.gym.avatar.myAvatarCollection.MyAvatarCollectionRepository;
import com.gym.category.Category;
import com.gym.config.exception.BaseException;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import com.gym.user.dto.GetMyPageRes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AvatarServiceTest {
    @Autowired UserRepository userRepository;
    @Autowired AvatarService avatarService;
    @Autowired UserService userService;


    @Test
    void setInitialAvatar() throws BaseException {
        User user = createTestUser();
        userRepository.save(user);
        avatarService.setInitialAvatar(user);

        Assertions.assertThat(user.getMyAvatar().getAvatarStep()).isEqualTo(AvatarStep.getInitialAvatarStep());
        GetMyPageRes myPage = userService.getMyPage(user.getUserId());
        Assertions.assertThat(myPage)
                .extracting("avatarImgUrl")
                .isEqualTo(AvatarStep.getInitialAvatarStep().getImgUrl());
    }

    @Test
    void userLevelUpTest() throws BaseException {
        User user = createTestUser();
        userRepository.save(user);
        avatarService.setInitialAvatar(user);

        AvatarStep levelUpAvatar = AvatarStep.AVATAR1_STEP2;
        avatarService.userLevelUp(user, levelUpAvatar);

        Assertions.assertThat(user.getMyAvatar().getAvatarStep()).isEqualTo(levelUpAvatar);
        GetMyPageRes myPage = userService.getMyPage(user.getUserId());
        Assertions.assertThat(myPage)
                .extracting("avatarImgUrl")
                .isEqualTo(AvatarStep.AVATAR1_STEP2.getImgUrl());
    }

    private User createTestUser() {
        User user = new User();
        user.update("test_user", "안녕");

        Category category = new Category();
        category.setCategoryId(1);
        user.updateSports(category);

        return user;
    }
}