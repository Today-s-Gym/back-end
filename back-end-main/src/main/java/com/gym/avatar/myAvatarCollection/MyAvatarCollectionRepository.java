package com.gym.avatar.myAvatarCollection;

import com.gym.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyAvatarCollectionRepository extends JpaRepository<MyAvatarCollection, Integer> {
    List<MyAvatarCollection> findByUser(User user);
}
