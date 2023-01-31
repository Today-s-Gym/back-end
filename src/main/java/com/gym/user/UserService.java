package com.gym.user;

import com.gym.avatar.avatar.Avatar;
import com.gym.avatar.avatar.AvatarStep;
import com.gym.avatar.avatar.MyAvatar;
import com.gym.avatar.avatar.MyAvatarRepository;
import com.gym.avatar.avatar.dto.MyAvatarDto;
import com.gym.avatar.myAvatarCollection.MyAvatarCollection;
import com.gym.avatar.myAvatarCollection.MyAvatarCollectionRepository;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.login.JwtProvider;
import com.gym.login.dto.JwtResponseDTO;
import com.gym.login.dto.UserUpdateRequestDTO;
import com.gym.record.RecordRepository;
import com.gym.user.dto.GetMyPageRes;
import com.gym.user.dto.UserRecordCount;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.gym.config.exception.BaseResponseStatus.*;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;


@Service
@RequiredArgsConstructor
public class UserService {
    private static int INTRODUCE_MAX_LENGTH = 30;

    private final UserRepository userRepository;
    private final UtilService utilService;
    private final MyAvatarCollectionRepository myAvatarCollectionRepository;
    private final RecordRepository recordRepository;
    private final MyAvatarRepository myAvatarRepository;
    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;

    /**
     * 사용자 공개 계정 전환
     */
    @Transactional
    public Integer changeAccountPrivacy(Integer userId, boolean locked) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);
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
    public void insertUser(User user) {
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

    @Transactional
    public User save(UserUpdateRequestDTO requestDTO) {
        return userRepository.save(requestDTO.toEntity());
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
    public List<MyAvatarDto> getMyCollection(Integer userId) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);
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
    public GetMyPageRes getMyPage(Integer userId) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String thisMonth = LocalDate.now().format(formatter);

        int thisMonthRecordCount = recordRepository.countByUserIdMonth(user.getUserId(), thisMonth);
        int totalRecordCount = user.getRecordCount();

        GetMyPageRes myPageInfo = new GetMyPageRes(
                user.getMyAvatar(),
                user.getNickName(),
                user.getCategory().getName(),
                user.getIntroduce(),
                user.isLocked());

        UserRecordCount userRecordCount = new UserRecordCount(
                thisMonthRecordCount,
                user.getMyAvatar().getRemainUpgradeCount(totalRecordCount),
                totalRecordCount);

        myPageInfo.setUserRecordCount(userRecordCount);

        return myPageInfo;
    }

    /**
     * 현재 내 아바타 이미지 조회
     */
    @Transactional(readOnly = true)
    public String getNowAvatarImg(Integer userId) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);
        return AvatarStep.findAvatarImg(user.getMyAvatar().getAvatarStep());
    }

    @Transactional
    public boolean checkAndMyAvatarLevelUp(Integer userId) {
        User user = userRepository.findWithMyAvatarByUserId(userId);
        int recordCount = user.getRecordCount();
        AvatarStep avatarStep = AvatarStep.findByRecordCount(recordCount);

        if (!user.getMyAvatar().getAvatarStep().equals(avatarStep)) {
            MyAvatar levelUpAvatar = myAvatarRepository.findByAvatarStep(avatarStep).get();
            saveMyAvatarInCollection(user, levelUpAvatar);
            user.changeAvatarStep(levelUpAvatar);
            return true;
        }
        return false;
    }

    @Transactional
    public void saveMyAvatarInCollection(User user, MyAvatar myAvatar) {
        MyAvatarCollection myAvatarCollection = new MyAvatarCollection();
        myAvatarCollection.setUser(user);
        myAvatarCollection.setMyAvatar(myAvatar);
        myAvatarCollectionRepository.save(myAvatarCollection);
    }

    @Transactional
    public GetMyPageRes getUserProfile(Integer userId) throws BaseException {
        User user = utilService.findByUserIdWithValidation(userId);
        if (user.isLocked()) {
            return GetMyPageRes.lockedMyPageInfo();
        }
        return getMyPage(userId);
    }


    //액세스 토큰, 리프레시 토큰 함께 재발급
    public BaseResponse<?> reissue(String refreshToken) {
        // 1. Refresh Token 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            return new BaseResponse<>(JWT_NOTVALID_ERROR);
        }

        // 2. Access Token 에서 User email 를 가져옵니다.
        Integer userid = jwtProvider.getAuthentication(refreshToken);
        String useremail = userRepository.findById(userid).get().getEmail();


        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshTokens = (String) redisTemplate.opsForValue().get("RT:" + useremail);
        // 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if (ObjectUtils.isEmpty(refreshToken)) {
            return new BaseResponse<>(NOT_VALID_ERROR);
        }
        if (!refreshToken.equals(refreshToken)) {
            return new BaseResponse<>(JWT_OTHER_ERROR);
        }

        // 4. 새로운 토큰 생성
        JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(userid);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);

        return new BaseResponse<>(tokenInfo);
    }

    public BaseResponse<?> logout(String accessToken) {
        // 1. Access Token 검증
        if (!jwtProvider.validateToken(accessToken)) {
            return new BaseResponse<>(JWT_NOTVALID_ERROR);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Integer userid = jwtProvider.getAuthentication(accessToken);
        String useremail = userRepository.findById(userid).get().getEmail();

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + useremail) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + useremail);
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtProvider.getExpiration(accessToken);
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return new BaseResponse<>(SUCCESS);
    }
}
