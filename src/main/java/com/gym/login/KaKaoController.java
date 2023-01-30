package com.gym.login;

import com.google.gson.Gson;
import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.login.dto.JwtResponseDTO;
import com.gym.login.dto.UserUpdateRequestDTO;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
public class KaKaoController {
    @Autowired
    private KaKaoService kaKaoLoginService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtController jwtController;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RedisTemplate redisTemplate;

    //카카오 로그인 코드
    @ResponseBody
    @GetMapping("/oauth/kakao")
    public BaseResponse<?> kakaoCallback(String code) throws Exception {
        String accessToken = kaKaoLoginService.getAccessToken(code);
        Gson gsonObj = new Gson();
        Map<?, ?> data = gsonObj.fromJson(accessToken, Map.class);
        String atoken = (String) data.get("access_token");
        Gson gsonObj2 = new Gson();
        Map<?, ?> data2 = gsonObj.fromJson(accessToken, Map.class);
        String refreshToken = (String) data.get("refresh_token");
        String useremail = kaKaoLoginService.getUserInfo(atoken, refreshToken);
        Optional<User> findUser = userRepository.findByEmail(useremail);
        if (findUser.isEmpty()) {
            UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(useremail);
            User kakaoUser = userService.save(userUpdateRequestDTO);
            JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(kakaoUser.getUserId());
            //JwtController.TokenDataResponse kakaorefreshToken = jwtController.createRefreshToken(kakaoUser.getUserId());

            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            redisTemplate.opsForValue()
                    .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);
            //kakaoUser.updateRefreshToken(kakaorefreshToken);
            //String loginToken = jwtController.createToken(kakaoUser.getUserId());
            userService.insertUser(kakaoUser);

            return new BaseResponse<>(tokenInfo);
        } else {
            User user = findUser.get();
            //JwtController.TokenDataResponse kakaorefreshToken = jwtController.createRefreshToken(user.getUserId());
            JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(user.getUserId());

            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            redisTemplate.opsForValue()
                    .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);
            //String loginToken = jwtController.createToken(user.getUserId());
            return new BaseResponse<>(tokenInfo);
        }

    }

    //카카오 로그아웃 코드
    @GetMapping("/oauth/kakaologout")
    @ResponseBody
    public BaseResponse<?> kakaoLogout(@RequestParam("state") String jwtToken)
    {
        try{
            return userService.logout(jwtToken);
        } catch(Exception e){
            return new BaseResponse<>(BaseResponseStatus.KAKAO_ERROR);
        }
    }
}
