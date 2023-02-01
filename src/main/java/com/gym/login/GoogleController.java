package com.gym.login;

import com.google.gson.Gson;
import com.gym.config.exception.BaseResponse;
import com.gym.login.dto.JwtResponseDTO;
import com.gym.login.dto.UserUpdateRequestDTO;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
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
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/login/google")
    public BaseResponse<?> GoogleCallback(String code){
        try{
            String accessToken = googleService.getAccessToken(code);
            Gson gsonObj = new Gson();
            Map<?, ?> data = gsonObj.fromJson(accessToken, Map.class);
            String atoken = (String) data.get("access_token");
            String useremail = googleService.getUserInfo(atoken);
            Optional<User> findUser = userRepository.findByEmail(useremail);
            if (findUser.isEmpty()) {
                UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(useremail);
                User googleUser = userService.save(userUpdateRequestDTO);
                JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(googleUser.getUserId());
                // RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
                redisTemplate.opsForValue()
                        .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);
                userService.insertUser(googleUser);

                return new BaseResponse<>(tokenInfo);
            } else {
                User user = findUser.get();
                JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(user.getUserId());

                // RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
                redisTemplate.opsForValue()
                        .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);
                return new BaseResponse<>(tokenInfo);
            }

        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;
        }



    }
}
