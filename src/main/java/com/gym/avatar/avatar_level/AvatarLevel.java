package com.gym.avatar.avatar_level;

import com.gym.avatar.avatar.Avatar;
import com.gym.avatar.my_avatar.MyAvatar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "avatar_level")
public class AvatarLevel {

    /** 지금은 아바타를 1개로 가정하기로 했기 때문에,
     *  avatarLevelId == level 이라고 생각하면 됩니다!
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer avatarLevelId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Avatar avatar;          // 아바타 엔티티와 연관관계
    private Integer level;          // 아바타 레벨
    private String imgUrl;          // 아바라 레벨에 해당하는 아바타 이미지

}
