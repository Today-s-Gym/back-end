package com.gym.login;

import com.google.gson.Gson;
import com.gym.config.exception.BaseResponse;
import com.gym.login.dto.JwtResponseDTO;
import com.gym.login.dto.UserUpdateRequestDTO;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GoogleController {
    private final GoogleService googleService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/login/google")
    public BaseResponse<?> GoogleCallback(String code) {
        try{
            log.info("구글 로그인 진입");
            String accessToken = googleService.getAccessToken(code);

            log.info("code로부터 token 추출 성공");

            Gson gsonObj = new Gson();
            Map<?, ?> data = gsonObj.fromJson(accessToken, Map.class);
            String atoken = (String) data.get("access_token");
            String useremail = googleService.getUserInfo(atoken);
            log.info("token으로부터 useremail 추출 성공");
            Optional<User> findUser = userRepository.findByEmail(useremail);
            if (findUser.isEmpty()) {
                log.info("구글 로그인 - 계정 새로 생성");
                //UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(useremail);
                //User googleUser = userService.save(userUpdateRequestDTO);
                User googleUser = new User();
                googleUser.updateEmail(useremail);
                JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(googleUser.getUserId());
                googleUser.updateRefreshToken(tokenInfo.getRefreshToken());
                userRepository.save(googleUser);
                // RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
/*                redisTemplate.opsForValue()
                        .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);*/
                userService.insertUser(googleUser);

                return new BaseResponse<>(tokenInfo);
            } else {
                log.info("구글 로그인 - 기존 회원 로그인");

                User user = findUser.get();
                JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(user.getUserId());
                user.updateRefreshToken(tokenInfo.getRefreshToken());
                userRepository.save(user);

                // RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
/*                redisTemplate.opsForValue()
                        .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);*/
                return new BaseResponse<>(tokenInfo);
            }

        } catch(Exception e){
            log.error(e.getMessage());
            return null;
        }



    }
}
