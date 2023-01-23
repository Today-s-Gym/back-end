package com.gym.user;

import com.gym.avatar.avatar.dto.MyAvatarDto;
import com.gym.config.exception.BaseException;
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
import java.util.List;

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

    @Test
    @DisplayName("마이페이지 변경")
    @Transactional
    void editMyPage() throws BaseException {
        // given
        Integer userId = 2;

        // when
        String newNickname = "유저2";
        String newIntroduce = "안녕하세요. 여러분. 반갑습니다.";
        userService.editMyPage(userId, newNickname, newIntroduce);

        // then
        User findByIdUser = userRepository.findById(userId).get();
        Assertions.assertThat(findByIdUser.getNickName()).isEqualTo(newNickname);
        Assertions.assertThat(findByIdUser.getIntroduce()).isEqualTo(newIntroduce);
    }

    @Test
    @DisplayName("마이페이지 변경_닉네임 중복")
    @Transactional
    void editMyPage_nickname_duplicated() throws BaseException {
        // given
        Integer userId = 2;

        // when
        String newNickname = "보라";
        String newIntroduce = "안녕하세요. 여러분. 반갑습니다.";
        Assertions.assertThatThrownBy(() -> userService.editMyPage(userId, newNickname, newIntroduce))
                .isInstanceOf(BaseException.class);

    }

    @Test
    @DisplayName("내 아바타 조회 테스트")
    @Transactional
    void getAvatarCollection() {
        User user = userRepository.getByUserId(1).get();

        List<MyAvatarDto> myCollection = userService.getMyCollection(user);
        for (MyAvatarDto myAvatarDto : myCollection) {
            System.out.println("====");
            System.out.println("myAvatarId: " + myAvatarDto.getMyAvatarId());
            System.out.println("avatarName: " + myAvatarDto.getAvatarName());
            System.out.println("avatarLevel: " + myAvatarDto.getLevel());
            System.out.println("avatarImg: " + myAvatarDto.getImgUrl());
        }
    }
}