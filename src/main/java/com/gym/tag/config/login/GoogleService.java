package com.gym.tag.config.login;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class GoogleService {
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
        body.add("client_id", "429955294987-388f8t5sfg593faspeftn44nkrthu8mc.apps.googleusercontent.com");
        body.add("client_secret", "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");
        body.add("redirect_uri" , "http://localhost:8000/login/google");
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

        //String jsonData = responseEntity.getBody();

/*        //JSON 데이터에서 액세스 토큰 정보만 추출
        Gson gsonObj = new Gson();
        Map<?, ?> data = gsonObj.fromJson(jsonData, Map.class);

        return (String) data.get("access_token");*/


    }

    public User getUserInfo(String access_Token) {
        //요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> googleUserInfo = new HashMap<>();
        //String reqURL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+access_Token;
        String reqURL = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + access_Token;
        User user = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                //JsonParser parser = new JsonParser();
                System.out.println("result : " + result);
                //JsonElement element = parser.parse(result);
                JsonElement element = JsonParser.parseString(result);

                //String name = element.getAsJsonObject().get("name").getAsString();
                String email = element.getAsJsonObject().get("email").getAsString();
                //String id = "GOOGLE_"+element.getAsJsonObject().get("id").getAsString();

                user = new User();
                //googleUserInfo.put("name", name);
                user.setEmail(email);
                //googleUserInfo.put("id", id);

                return user;


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }
}
