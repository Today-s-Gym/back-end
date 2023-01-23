package com.gym.post;

import com.gym.category.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> getByPostId(Integer postId);

    @Query(value = "SELECT p FROM Post p WHERE p.category= :category ORDER BY p.postId DESC")
    Optional<List<Post>> findByCategoryId(Category category, Pageable limit);

    @Modifying
    @Query("delete from Post p where p.postId = :postId")
    Integer deleteAllByPostId(Integer postId);
}
