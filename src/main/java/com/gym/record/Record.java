package com.gym.record;

import com.gym.post.Post;
import com.gym.record.photo.RecordPhoto;
import com.gym.tag.Tag;
import com.gym.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean deleted = false;
    private int report = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "record", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Post> postList;

    @OneToMany(mappedBy = "record", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<RecordPhoto> photoList;

    @OneToMany(mappedBy = "record", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Tag> tagList;

}
