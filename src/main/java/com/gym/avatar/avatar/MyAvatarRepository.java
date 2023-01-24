package com.gym.avatar.avatar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyAvatarRepository extends JpaRepository<MyAvatar, Integer> {
    List<MyAvatar> findByAvatarStep(AvatarStep avatarStep);

}
