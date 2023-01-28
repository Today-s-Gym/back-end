package com.gym.login;


import com.gym.category.Category;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
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

    @Autowired(required = false)
    private ModelMapper modelMapper;

    @PutMapping("/login/sports")
    @ResponseBody
    public BaseResponse<?> uploadSports(@RequestParam("userId") int userId, @RequestParam("categoryId") int categoryId)

    {
        try{
            User user = utilService.findByUserIdWithValidation(userId);
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
