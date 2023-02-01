package com.gym.login;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gym.user.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static com.gym.secret.Secret.Google_Client_Id;
import static com.gym.secret.Secret.Google_Secret_Password;

@Service
public class GoogleService {

    public String getAccessToken(String code){
        //HttpHeaders 생성00
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", Google_Client_Id);
        body.add("client_secret", Google_Secret_Password);
        body.add("redirect_uri" , "http://todaysgym.shop/login/google");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return responseEntity.getBody();
    }

    public String getUserInfo(String accessToken) {
        //요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> googleUserInfo = new HashMap<>();
        String reqURL = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        User user = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                JsonElement element = JsonParser.parseString(result);
                String email = element.getAsJsonObject().get("email").getAsString();

                return email;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
