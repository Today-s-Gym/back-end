package com.gym.login;

import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class NicknameController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login/nickname")
    @ResponseBody
    public BaseResponse<?> updateNickname(@RequestParam("nickname") String nickname)
    {

        Optional<User> findUser = userRepository.findByNickName(nickname);
        if(findUser.isEmpty()){

            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }
        else{
            return new BaseResponse<>(BaseResponseStatus.NICKNAME_ERROR);
        }
    }



/*    public void updateNickname(User user, String nickname)
    {
        Optional<User> findUser = userRepository.findByNickName(nickname);
        if(findUser.isEmpty()){
            user.setNickName(nickname);
            userRepository.save(user);
            System.out.println("닉네임 생성 완료");
        }
        else{
            System.out.println("이미 존재하는 닉네임입니다.");
        }
    }*/
}
