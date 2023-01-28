package com.gym.login;

import com.google.gson.Gson;
import com.gym.login.dto.UserUpdateRequestDTO;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;

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


    @Autowired(required = false)
    private ModelMapper modelMapper;

    @ResponseBody
    @GetMapping("/login/google")
    public String GoogleCallback(String code) throws Exception {
        String accessToken = googleService.getAccessToken(code);

        Gson gsonObj = new Gson();
        Map<?, ?> data = gsonObj.fromJson(accessToken, Map.class);

        String atoken = (String) data.get("access_token");

        String useremail = googleService.getUserInfo(atoken);

        Optional<User> findUser = userRepository.findByEmail(useremail);
        if (findUser.isEmpty()) {
            UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(useremail);
            User googleUser = userService.save(userUpdateRequestDTO);

            String refreshToken = jwtController.createRefreshToken(googleUser.getUserId());
            String loginToken = jwtController.createToken(googleUser.getUserId());
            googleUser.updateRefreshToken(refreshToken);
            //System.out.println("refreshToken = " + refreshToken);
            userService.insertUser(googleUser);
            return loginToken;
            //System.out.println(googleUser.getUserId());
        } else {
            User user = findUser.get();
            String loginToken = jwtController.createToken(user.getUserId());
            //System.out.println("loginToken = " + loginToken);
            //googleUser.setDeviceToken(loginToken);
            return loginToken;
        }


    }
}
