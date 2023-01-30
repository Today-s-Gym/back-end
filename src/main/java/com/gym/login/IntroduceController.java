package com.gym.login;


import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//@RequiredArgsConstructor
@Controller
public class IntroduceController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilService utilService;

    @Autowired
    private JwtService jwtService;

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
