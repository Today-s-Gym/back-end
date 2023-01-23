package com.gym.user;


import com.gym.tag.config.exception.BaseException;
import com.gym.tag.config.exception.BaseResponse;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

import static com.gym.tag.config.exception.BaseResponseStatus.DUPLICATED_NICKNAME;
import static com.gym.tag.config.exception.BaseResponseStatus.LENGTH_OVER_INTRODUCE;


@Service
@RequiredArgsConstructor
public class UserService {

    private static int INTRODUCE_MAX_LENGTH = 30;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilService utilService;

    /**
     * 사용자 공개 계정 전환
     */
    @Transactional
    public Integer changeAccountPrivacy(Integer userId, boolean locked) {
        User user = userRepository.findById(userId).get();
        user.changeAccountPrivacy(locked);
        return user.getUserId();
    }

    /**
     * 사용자 이메일 조회
     */
    @Transactional
    public String findUserEmailByUserId(Integer userId) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);
        return user.getEmail();
    }



    @Transactional
    public void insertUser(User user)
    {
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String useremail) {
        //검색 결과가 없을 때 빈 User 객체 반환
        User findUser = userRepository.findByEmail(useremail).orElseGet(
                new Supplier<User>() {
                    @Override
                    public User get() {
                        return new User();
                    }
                });

        return findUser;
    }


    /**
     * 마이페이지 수정
    */
    @Transactional
    public BaseResponse<Integer> editMyPage(Integer userId, String newNickname, String newIntroduce) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);
        editNickName(newNickname, user);
        editIntroduce(newIntroduce, user);
        return new BaseResponse<>(user.getUserId());
    }

    private void editNickName(String newNickname, User user) throws BaseException {
        if (user.getNickName().equals(newNickname)) {
            return;
        }
        if (userRepository.findByNickName(newNickname).isPresent()) {
            throw new BaseException(DUPLICATED_NICKNAME);
        }
        user.changeNickname(newNickname);
    }

    private static void editIntroduce(String newIntroduce, User user) throws BaseException {
        if (newIntroduce.length() > INTRODUCE_MAX_LENGTH) {
            throw new BaseException(LENGTH_OVER_INTRODUCE);
        }
        user.editIntroduce(newIntroduce);
    }


}
