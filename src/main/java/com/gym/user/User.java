package com.gym.user;

import com.gym.avatar.my_avatar.MyAvatar;
import com.gym.category.Category;
import com.gym.post.Post;
import com.gym.post.comment.Comment;
import com.gym.record.Record;
import com.gym.tag.Tag;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String nickName;
    private String introduce;
    private String email;
    private String refreshToken;
    private String deviceToken;
    private boolean locked = false; // false = 공개, true = 비공개

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean deleted = false;
    private int report = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Record> recordList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<MyAvatar> myAvatarList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Tag> tagList = new ArrayList<>();

    // == 메서드 추가 == //
    public void changeAccountPrivacy(boolean locked) {
        this.locked = locked;
    }

    public void addReportCount() {
        this.report++;
    }

    public void editIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void changeNickname(String newNickName) {
        this.nickName = newNickName;
    }
}
