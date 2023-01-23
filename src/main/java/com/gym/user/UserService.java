package com.gym.user;

import com.gym.avatar.avatar.Avatar;
import com.gym.avatar.avatar.MyAvatar;
import com.gym.avatar.avatar.dto.MyAvatarDto;
import com.gym.avatar.myAvatarCollection.MyAvatarCollection;
import com.gym.avatar.myAvatarCollection.MyAvatarCollectionRepository;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.record.RecordRepository;
import com.gym.user.dto.GetMyPageRes;
import com.gym.user.dto.UserRecordCount;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static com.gym.config.exception.BaseResponseStatus.DUPLICATED_NICKNAME;
import static com.gym.config.exception.BaseResponseStatus.LENGTH_OVER_INTRODUCE;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {

    private static int INTRODUCE_MAX_LENGTH = 30;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilService utilService;
    private final MyAvatarCollectionRepository myAvatarCollectionRepository;
    private final RecordRepository recordRepository;

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

    @Transactional(readOnly = true)
    public List<MyAvatarDto> getMyCollection(User user) {
        List<MyAvatarCollection> myAvatarCollections = myAvatarCollectionRepository.findByUser(user);
        Map<Avatar, MyAvatar> collect = myAvatarCollections.stream()
                .map(MyAvatarCollection::getMyAvatar)
                .collect(groupingBy(MyAvatar::getAvatar,
                        collectingAndThen(
                                maxBy(comparingInt(r -> r.getAvatarStep().getMaxRecordCount())),
                                Optional::get)));
        return collect.values().stream().map(MyAvatarDto::new).collect(toList());
    }

    @Transactional
    public GetMyPageRes getMyPage(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String thisMonth = LocalDate.now().format(formatter);

        int thisMonthRecordCount = recordRepository.countByUserIdMonth(user.getUserId(), thisMonth);
        int totalRecordCount = recordRepository.countByUserId(user.getUserId());
        GetMyPageRes myPageInfo = userRepository.findMyPageInfo(user.getUserId());
        UserRecordCount userRecordCount = new UserRecordCount(thisMonthRecordCount,
                user.getMyAvatar().getRemainUpgradeCount(totalRecordCount),
                totalRecordCount);
        myPageInfo.setUserRecordCount(userRecordCount);
        return myPageInfo;
    }
}
