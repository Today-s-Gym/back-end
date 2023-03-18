package com.gym.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gym.post.Post;
import com.gym.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<User> userList = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

}
