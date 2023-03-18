package com.gym.post.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    @Query(value = "SELECT l FROM Like l WHERE l.userId = :userId AND l.postId = :postId")
    Optional<Like> getLikeByUserIdAndCardId(Integer userId, Integer postId);

    @Query(value = "SELECT COUNT(l) FROM Like l WHERE l.postId = :postId AND l.status = true")
    Optional<Integer> getLikeCountsByPostId(Integer postId);
}
