package com.gym.avatar.avatar;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface MyAvatarRepository extends JpaRepository<MyAvatar, Integer> {
    Optional<MyAvatar> findByAvatarStep(AvatarStep avatarStep);
}
