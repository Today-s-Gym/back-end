package com.gym.login;

import com.google.gson.Gson;
import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.login.dto.JwtResponseDTO;
import com.gym.login.dto.UserUpdateRequestDTO;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KaKaoController {

    private final KaKaoService kaKaoLoginService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;

    //카카오 로그인 코드
    @ResponseBody
    @PostMapping("/oauth/kakao")
    public BaseResponse<?> kakaoCallback(@RequestParam("token") String accessToken) {
        try{
            //log.info("카카오 로그인 진입");
            //String accessToken = kaKaoLoginService.getAccessToken(code);

            log.info("accessToken 받아짐");

            //Gson gsonObj = new Gson();
            //Map<?, ?> data = gsonObj.fromJson(accessToken, Map.class);
            //String atoken = (String) data.get("access_token");

            String useremail = kaKaoLoginService.getUserInfo(accessToken);
            log.info("token으로부터 useremail 추출 성공");
            Optional<User> findUser = userRepository.findByEmail(useremail);


            if (findUser.isEmpty()) {
                log.info("카카오 로그인 - 계정 새로 생성");
                //UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(useremail);
                //User kakaoUser = userService.save(userUpdateRequestDTO);
                User kakaoUser = new User();
                kakaoUser.updateEmail(useremail);
                JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(kakaoUser.getUserId());
                kakaoUser.updateRefreshToken(tokenInfo.getRefreshToken());
                userRepository.save(kakaoUser);

                // RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
/*                redisTemplate.opsForValue()
                        .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);*/
                //userService.insertUser(kakaoUser);
                return new BaseResponse<>(tokenInfo);
            } else {
                log.info("카카오 로그인 - 기존 회원 로그인");
                User user = findUser.get();
                JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(user.getUserId());
                user.updateRefreshToken(tokenInfo.getRefreshToken());
                userRepository.save(user);
                //RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
/*                redisTemplate.opsForValue()
                        .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);*/
                return new BaseResponse<>(tokenInfo);
            }

        } catch(Exception e){
            log.error(e.getMessage());
            return null;
        }

    }

    //카카오 로그아웃 코드
/*    @GetMapping("/oauth/kakaologout")
    @ResponseBody
    public BaseResponse<?> kakaoLogout(@RequestParam("state") String jwtToken)
    {
        try{
            return userService.logout(jwtToken);
        } catch(Exception e){
            return new BaseResponse<>(BaseResponseStatus.KAKAO_ERROR);
        }
    }*/
}
