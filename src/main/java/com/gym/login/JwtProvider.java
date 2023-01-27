package com.gym.login;


import com.gym.secret.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtProvider {

    private Key key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(Secret.JWT_SECRET_KEY));

    //==토큰 생성 메소드==//
    public String createToken(Integer userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofDays(1).toMillis()); // 만료기간 1일

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
                .claim("userId", userId)
                .setIssuer("test") // 토큰발급자(iss)
                .setIssuedAt(now) // 발급시간(iat)
                .setExpiration(expiration) // 만료시간(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Integer userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofDays(365).toMillis()); // 만료기간 365일

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
                .claim("userId", userId)
                .setIssuer("test") // 토큰발급자(iss)
                .setIssuedAt(now) // 발급시간(iat)
                .setExpiration(expiration) // 만료시간(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //==Jwt 토큰의 유효성 체크 메소드==//
    public Claims parseJwtToken(String token) {
        token = BearerRemove(token); // Bearer 제거
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //==토큰 앞 부분('Bearer') 제거 메소드==//
    private String BearerRemove(String token) {
        return token.substring("Bearer ".length());
    }
}
