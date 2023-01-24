package com.gym.user;

import com.gym.avatar.avatar.MyAvatar;
import com.gym.user.dto.GetMyPageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickname);

    Optional<User> getByUserId(Integer userId);

    @Query("select new com.gym.user.dto.GetMyPageRes(u.myAvatar, u.nickName, c.name, u.introduce, u.locked)" +
            " from User u join u.category c where u.userId=:id")
    GetMyPageRes findMyPageInfo(@Param("id") Integer id);

    @Query("select u from User u join fetch u.myAvatar where u.userId=:id")
    User findWithMyAvatarByUserId(@Param("id") Integer id);
}
