package com.gym.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> getByPostId(Integer postId);

    @Query(value = "SELECT p FROM Post p WHERE p.category= :categoryId ORDER BY p.postId DESC")
    Optional<List<Post>> findByCategoryId(Integer categoryId, Pageable limit);
}
