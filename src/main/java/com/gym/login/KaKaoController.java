package com.gym.login;

import com.google.gson.Gson;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;

@Controller
public class KaKaoController {
    @Autowired
    private KaKaoService kaKaoLoginService;

    @Autowired
    private UserService userService;

    @Value("${kakao.default.password}")
    private String kakaoPassword;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtController jwtController;





/*    @ResponseBody
    @GetMapping("/oauth/kakao")
    public void kakaoCallback(@RequestParam String code) throws BaseException {
        System.out.println(code);

    }*/



    @ResponseBody
    @GetMapping("/oauth/kakao")
    public String kakaoCallback(String code) throws Exception {
        String accessToken = kaKaoLoginService.getAccessToken(code);
        Gson gsonObj = new Gson();
        Map<?, ?> data = gsonObj.fromJson(accessToken, Map.class);

        String atoken = (String) data.get("access_token");


        /*        List<User> findUser = userRepository.findByEmail(email);
        boolean isNotEmpty = findUser != null && !findUser.isEmpty();
        if (isNotEmpty) {
            System.out.println("이미 존재하는 회원입니다.");
        } else {
            userService.insertUser(user);
        }*/






        Gson gsonObj2 = new Gson();
        Map<?, ?> data2 = gsonObj.fromJson(accessToken, Map.class);

        String refreshToken = (String) data.get("refresh_token");

        User kakaoUser = kaKaoLoginService.getUserInfo(atoken, refreshToken);

        User findUser = userService.getUserByEmail(kakaoUser.getEmail());
        if(findUser.getEmail() == null){
            String kakaorefreshToken = jwtController.createRefreshToken(String.valueOf(kakaoUser.getUserId()));
            //System.out.println("refreshToken = " + refreshToken);
            kakaoUser.setRefreshToken(kakaorefreshToken);
            userService.insertUser(kakaoUser);
        }
        else{
            String loginToken = jwtController.createToken(String.valueOf(kakaoUser.getUserId()));
            //System.out.println("loginToken = " + loginToken);
            User user = userRepository.findByEmail(findUser.getEmail()).get();
            user.setDeviceToken(loginToken);
            //userRepository.save(user);
            return loginToken;

        }
        


        return null;


    }

}
