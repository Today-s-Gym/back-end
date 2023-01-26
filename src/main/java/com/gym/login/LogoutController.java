/*
package com.gym.login;

import com.gym.category.Category;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LogoutController {
    @Autowired
    private UtilService utilService;

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/login/logout")
    @ResponseBody
    public BaseResponse<?> logout(@RequestParam("userid") int userid)

    {
        try{
            User user = utilService.findByUserIdWithValidation(userid);
            user.setDeviceToken(null);
            userRepository.save(user);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}*/
