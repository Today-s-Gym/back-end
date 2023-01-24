package com.gym.post.like;

import com.gym.config.exception.BaseException;
import com.gym.post.like.dto.PushLikeRes;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UtilService utilService;

    @Transactional
    public void save(Like like) {
        likeRepository.save(like);
    }

    public void createLike(Integer userId, Integer postId) {
        Like like = Like.builder()
                .userId(userId)
                .postId(postId)
                .status(true)
                .build();
        save(like);
    }

    public boolean checkLike(Integer userId, Integer postId) {
        Like like = likeRepository.getLikeByUserIdAndCardId(userId, postId).orElse(null);
        //비어있으면 안 누른 거!!
        if (like == null) {
            return false;
        } else {
            if(!like.isStatus()) return false;
        }
        return true;
    }

    public Integer getLikeCounts(Integer postId) {
        return likeRepository.getLikeCountsByPostId(postId).orElse(0);
    }


    @Transactional
    public PushLikeRes pushLike(Integer userId, Integer postId) throws BaseException {
        utilService.findByPostIdWithValidation(postId);
        Like like = likeRepository.getLikeByUserIdAndCardId(userId, postId).orElse(null);

        String result = "";
        // 새로 누르는 좋아요
        if (like == null) {
            createLike(userId, postId);
            result = "좋아요를 누름";
        }
        // 좋아요 상태 변경
        else {
            like.changeStatus();
            result = "좋아요 상태 변경 성공";
        }
        return new PushLikeRes(userId, postId, result);
    }

}
