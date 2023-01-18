package com.gym.login;


import com.gym.category.Category;
import com.gym.category.CategoryService;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SportsController {

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/login/sports")
    public @ResponseBody void uploadSports(@RequestBody User user, int categoryid)
    {
        Category category = new Category();
        category.setCategoryId(categoryid);
        user.setCategory(category);
        userRepository.save(user);

    }
}
