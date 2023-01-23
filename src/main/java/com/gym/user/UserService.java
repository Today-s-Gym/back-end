package com.gym.user;

import com.gym.tag.config.exception.BaseException;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UtilService utilService;

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


}
