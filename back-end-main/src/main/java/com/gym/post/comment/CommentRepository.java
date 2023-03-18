package com.gym.post.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> getByCommentId(Integer commentId);

    @Modifying
    @Query("delete from Comment c where c.commentId = :commentId")
    Integer deleteComment(@Param("commentId") Integer commentId);
}
