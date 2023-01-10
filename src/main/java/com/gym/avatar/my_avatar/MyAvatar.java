package com.gym.avatar.my_avatar;

import com.gym.avatar.avatar_level.AvatarLevel;
import com.gym.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "my_avatar")
public class MyAvatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer myAvatarId;

    private Integer avatarLevelId;  // 이 아바타가 몇 레벨까지 있는지, 어떤 이미지를 가지는지 나타내는 Id

   private int myLevel;            // 유저가 이 아바타를 얼마나 키웠는지를 나타내는 level

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
