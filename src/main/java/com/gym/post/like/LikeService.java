package com.gym.post.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

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


}
