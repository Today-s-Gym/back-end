package com.gym.post.photo;
;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostPhotoRepository  extends JpaRepository<PostPhoto, Integer> {


    @Query("select p from PostPhoto p where p.post.postId = :postId")
    List<PostPhoto> findAllByPost(@Param("postId") Integer postId);

    @Query("select pp.id from PostPhoto pp where pp.post.postId = :postId")
    List<Integer> findAllId(@Param("postId") Integer postId);

    @Query("select pp.imgUrl from PostPhoto pp where pp.post.postId = :postId")
    List<String> findAllPhotos(@Param("postId") Integer postId);

    @Modifying
    @Query("delete from PostPhoto pp where pp.id in :ids")
    Integer deleteAllByPost(@Param("ids") List<Integer> ids);
}
