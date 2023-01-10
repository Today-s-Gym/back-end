package com.gym.category;

import com.gym.post.Post;
import com.gym.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    private String name;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<User> userList;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Post> postList;
}
