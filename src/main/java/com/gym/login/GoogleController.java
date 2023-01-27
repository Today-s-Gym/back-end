package com.gym.login;

import com.google.gson.Gson;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class GoogleController {
    @Autowired
    private GoogleService googleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtController jwtController;

    @ResponseBody
    @GetMapping("/login/google")
    public String GoogleCallback(String code) throws Exception {
        String accessToken = googleService.getAccessToken(code);

        Gson gsonObj = new Gson();
        Map<?, ?> data = gsonObj.fromJson(accessToken, Map.class);

        String atoken = (String) data.get("access_token");

        User googleUser = googleService.getUserInfo(atoken);

        User findUser = userService.getUserByEmail(googleUser.getEmail());
        if (findUser.getEmail() == null) {

            String refreshToken = jwtController.createRefreshToken(googleUser.getUserId());
            //System.out.println("refreshToken = " + refreshToken);
            googleUser.setRefreshToken(refreshToken);
            userService.insertUser(googleUser);
            //System.out.println(googleUser.getUserId());
        } else {
            String loginToken = jwtController.createToken(googleUser.getUserId());
            //System.out.println("loginToken = " + loginToken);
            googleUser.setDeviceToken(loginToken);
            return loginToken;
        }
        return null;

    }
}
