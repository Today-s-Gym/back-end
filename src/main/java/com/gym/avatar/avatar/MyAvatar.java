package com.gym.avatar.avatar;

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
@Table(name = "my_avatar")
public class MyAvatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_avatar_id")
    private Integer myAvatarId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;          // 아바타 엔티티와 연관관계

    @Enumerated(EnumType.STRING)
    private AvatarStep avatarStep;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean deleted = false;

    public int getRemainUpgradeCount(int recordCount) {
        return this.avatarStep.getRemainUpgradeCount(recordCount);
    }

}
