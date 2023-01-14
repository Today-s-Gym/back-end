package com.gym.utils;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.post.Post;
import com.gym.post.PostRepository;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UtilService {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public User findByUserIdWithValidation(Integer userId) throws BaseException {
        User user = userRepository.getByUserId(userId).orElse(null);
        if(user == null) throw new BaseException(BaseResponseStatus.INVALID_USER);
        return user;
    }

    public Post findByPostIdWithValidation(Integer postId) throws BaseException {
        Post post = postRepository.getByPostId(postId).orElse(null);
        if(post == null) throw new BaseException(BaseResponseStatus.INVALID_POST);
        return post;
    }

    // 게시글에 첨부된 기록에 사진이 없을 시에 기본으로 뜨는 이미지를 나타냄!!!
    public static String returnRecordBaseImage() {
        return "기본 이미지 URL";
    }

    public static String convertLocalDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public static String convertLocalDateTimeToTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        long diffTime = localDateTime.until(now, ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -

        String msg = null;
        if (diffTime < SEC){
            return diffTime + "초전";
        }
        diffTime = diffTime / SEC;
        if (diffTime < MIN) {
            return diffTime + "분 전";
        }
        diffTime = diffTime / MIN;
        if (diffTime < HOUR) {
            return diffTime + "시간 전";
        }
        diffTime = diffTime / HOUR;
        if (diffTime < DAY) {
            return diffTime + "일 전";
        }
        diffTime = diffTime / DAY;
        if (diffTime < MONTH) {
            return diffTime + "개월 전";
        }

        diffTime = diffTime / MONTH;
        return diffTime + "년 전";
    }
}
