package com.gym.login;


import com.gym.avatar.AvatarService;
import com.gym.avatar.avatar.AvatarStep;
import com.gym.avatar.avatar.MyAvatar;
import com.gym.avatar.avatar.MyAvatarRepository;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IntroduceController {
    private final UserRepository userRepository;
    private final MyAvatarRepository myAvatarRepository;

    private final UtilService utilService;
    private final JwtService jwtService;
    private final UserService userService;
    private final AvatarService avatarService;

    @PutMapping("/login/introduce")
    @ResponseBody
    public BaseResponse<?> updateIntroduce(@RequestParam("nickname") String nickname, @RequestParam("introduce") String introduce)
    {
        try{
            Integer userid = jwtService.getUserIdx();
            User user = utilService.findByUserIdWithValidation(userid);

            if((introduce.length() >= 0) && (introduce.length() <= 30)){
                user.update(nickname, introduce);
                userRepository.save(user);
                return new BaseResponse<>(BaseResponseStatus.SUCCESS);
            }
            else{
                return new BaseResponse<>(BaseResponseStatus.INTRODUCE_ERROR);

            }
        }
        catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }
}
