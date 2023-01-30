package com.gym.login;


import com.gym.category.Category;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequiredArgsConstructor
public class SportsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private JwtService jwtService;

    @PutMapping("/login/sports")
    @ResponseBody
    public BaseResponse<?> uploadSports(@RequestParam("categoryid") int categoryId)

    {
        try{
            Integer userid = jwtService.getUserIdx();
            User user = utilService.findByUserIdWithValidation(userid);
            Category category = new Category();
            category.setCategoryId(categoryId);
            user.updateSports(category);
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
