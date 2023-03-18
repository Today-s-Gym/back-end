package com.gym.post;

import com.gym.category.Category;
import com.gym.post.comment.Comment;
import com.gym.post.photo.PostPhoto;
import com.gym.record.Record;
import com.gym.record.photo.RecordPhoto;
import com.gym.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    private String title;
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean deleted = false;
    private int report = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PostPhoto> photoList = new ArrayList<>();

    //==메서드 추가==//
    public void addReportCount() {
        this.report++;
    }

    //==비즈니스 로직==//
    public void updatePost(String title, String content){
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateRecord(Record record){
        this.record = record;
    }

    //==연관관계 메서드==//
    public void addPhotoList(PostPhoto postPhoto){
        photoList.add(postPhoto);
        postPhoto.createPost(this);
    }

}
