package com.gym.login;

import com.google.gson.Gson;
import com.gym.login.dto.UserUpdateRequestDTO;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import org.modelmapper.ModelMapper;
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

        String useremail = kaKaoLoginService.getUserInfo(atoken, refreshToken);

        Optional<User> findUser = userRepository.findByEmail(useremail);
        if (findUser.isEmpty()) {
            UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(useremail);
            User kakaoUser = userService.save(userUpdateRequestDTO);




            String kakaorefreshToken = jwtController.createRefreshToken(kakaoUser.getUserId());
            kakaoUser.updateRefreshToken(kakaorefreshToken);
            String loginToken = jwtController.createToken(kakaoUser.getUserId());
            //System.out.println("refreshToken = " + refreshToken);

            userService.insertUser(kakaoUser);
            return loginToken;

        } else {
            User user = findUser.get();
            String loginToken = jwtController.createToken(user.getUserId());
            System.out.println("loginToken = " + loginToken);
            //kakaoUser.setDeviceToken(loginToken);
            return loginToken;
        }

    }
}
