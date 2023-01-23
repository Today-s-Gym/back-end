package com.gym.login;


import com.google.gson.Gson;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class KaKaoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public String getAccessToken(String code){
        //HttpHeaders 생성00
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "5ad77a01169ec3d9832a0f099bb505b7");
        body.add("redirect_uri" , "http://localhost:8000/oauth/kakao");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return responseEntity.getBody();

        //String jsonData = responseEntity.getBody();

/*        //JSON 데이터에서 액세스 토큰 정보만 추출
        Gson gsonObj = new Gson();
        Map<?, ?> data = gsonObj.fromJson(jsonData, Map.class);

        return (String) data.get("access_token");*/


    }

    public User getUserInfo(String accessToken, String refreshToken) {
        //Httpheader 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //Httpheader와 HttpBody를 하나의 객체에 담기(body 정보는 생략 가능)
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(httpHeaders);

        //RestTemplate를 이용하면 브라우저 없이 HTTP 요청을 처리할 수 있다.
        RestTemplate restTemplate = new RestTemplate();

        //Http 요청을 post(GET) 방식으로 실행 -> 문자열로 응답이 들어온다.
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //카카오 인증 서버가 반환한 사용자 정보
        String userInfo = responseEntity.getBody();

        //JSON 데이터에서 추출한 정보로 User 객체 설정
        Gson gsonObj = new Gson();
        Map<?, ?> data = gsonObj.fromJson(userInfo, Map.class);

        Double id = (Double)(data.get("id"));
        String email = (String) ((Map<?, ?>)(data.get("kakao_account"))).get("email");

        User user = new User();
        user.setEmail(email);
        user.setRefreshToken(refreshToken);

        return user;




    }
}



