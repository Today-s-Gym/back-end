package com.gym.avatar.my_avatar;

import com.gym.avatar.avatar_level.AvatarLevel;
import com.gym.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_level_id")
    private AvatarLevel avatarLevel;  // 이 아바타가 몇 레벨까지 있는지, 어떤 이미지를 가지는지 나타내는 Id

    private int myLevel;            // 유저가 이 아바타를 얼마나 키웠는지를 나타내는 level

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
