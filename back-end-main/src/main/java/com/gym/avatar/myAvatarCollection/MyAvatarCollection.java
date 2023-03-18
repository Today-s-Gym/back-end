package com.gym.avatar.myAvatarCollection;

import com.gym.avatar.avatar.MyAvatar;
import com.gym.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "my_avatar_collection")
public class MyAvatarCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer myAvatarCollectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_avatar_id")
    private MyAvatar myAvatar;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean deleted = false;
}
