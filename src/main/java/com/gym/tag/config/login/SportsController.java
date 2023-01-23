package com.gym.tag.config.login;


import com.gym.category.Category;
import com.gym.tag.config.exception.BaseResponse;
import com.gym.tag.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SportsController {

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/login/sports")
    @ResponseBody
    public BaseResponse<?> uploadSports(@RequestBody User user, @RequestParam("categoryid") int categoryid)
    {

        Category category = new Category();
        category.setCategoryId(categoryid);
        user.setCategory(category);
        userRepository.save(user);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);

    }


/*    public void uploadSports(int userid, int categoryid)
    {
        User user = userRepository.findById(userid).get();
        Category category = new Category();
        category.setCategoryId(categoryid);
        user.setCategory(category);
        userRepository.save(user);

    }*/
}
