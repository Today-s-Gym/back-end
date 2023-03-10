package com.gym.login.kakao;

import com.google.gson.Gson;
import com.gym.avatar.AvatarService;
import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.login.jwt.JwtProvider;
import com.gym.login.dto.JwtResponseDTO;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KaKaoController {

    private final KaKaoService kaKaoLoginService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;
    private final AvatarService avatarService;


    //카카오 로그인 코드
    @ResponseBody
    @PostMapping("/oauth/kakao")
    public BaseResponse<?> kakaoCallback(@RequestParam("token") String accessToken) {
        try{
            //String accessToken = kaKaoLoginService.getAccessToken(code);
            String useremail = kaKaoLoginService.getUserInfo(accessToken);
            Optional<User> findUser = userRepository.findByEmail(useremail);


            if (findUser.isEmpty()) {
                //UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(useremail);
                //User kakaoUser = userService.save(userUpdateRequestDTO);
                User kakaoUser = new User();
                kakaoUser.updateEmail(useremail);
                JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(kakaoUser.getUserId());
                kakaoUser.updateRefreshToken(tokenInfo.getRefreshToken());
                userRepository.save(kakaoUser);
                avatarService.setInitialAvatar(kakaoUser);

                // RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
                //redisTemplate.opsForValue()
                //        .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);
                //userService.insertUser(kakaoUser);
                return new BaseResponse<>(tokenInfo);
            } else {
                User user = findUser.get();
                JwtResponseDTO.TokenInfo tokenInfo = jwtProvider.generateToken(user.getUserId());
                user.updateRefreshToken(tokenInfo.getRefreshToken());
                userRepository.save(user);
                //RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
                //redisTemplate.opsForValue()
                //        .set("RT:" + useremail, tokenInfo.getRefreshToken(), jwtProvider.getExpiration(tokenInfo.getRefreshToken()), TimeUnit.MILLISECONDS);
                return new BaseResponse<>(tokenInfo);
            }

        } catch(Exception e){
            log.error(e.getMessage());
            return null;
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
