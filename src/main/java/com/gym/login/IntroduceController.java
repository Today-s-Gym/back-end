package com.gym.login;


import com.gym.tag.config.exception.BaseResponse;
import com.gym.tag.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IntroduceController {

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/login/introduce")
    @ResponseBody
    public BaseResponse<?> updateIntroduce(@RequestBody User user, @RequestParam("nickname") String nickname, @RequestParam("introduce") String introduce)
    {

        if((introduce.length() >= 0) && (introduce.length() <= 30)){
            user.setNickName(nickname);
            user.setIntroduce(introduce);
            userRepository.save(user);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);

        }
        else{
            return new BaseResponse<>(BaseResponseStatus.INTRODUCE_ERROR);

        }
    }
}
