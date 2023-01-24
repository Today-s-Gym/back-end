package com.gym.login;


import com.gym.category.Category;
import com.gym.tag.config.exception.BaseException;
import com.gym.tag.config.exception.BaseResponse;
import com.gym.tag.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequiredArgsConstructor
public class SportsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilService utilService;

    @PutMapping("/login/sports")
    @ResponseBody
    public BaseResponse<?> uploadSports(@RequestParam("userid") int userid, @RequestParam("categoryid") int categoryid)

    {
        try{
            User user = utilService.findByUserIdWithValidation(userid);
            Category category = new Category();
            category.setCategoryId(categoryid);
            user.setCategory(category);
            userRepository.save(user);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
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
