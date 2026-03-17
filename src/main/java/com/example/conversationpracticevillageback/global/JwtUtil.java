package com.example.conversationpracticevillageback.global;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "villageKey";
    private static final String ISSUER = "villageIssuer";

    // Access Token 생성 (30분)
    public String createAccessToken(Long memberId) {
        return JWT.create()
                .withSubject(memberId.toString())
                .withIssuer(ISSUER)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .sign(Algorithm.HMAC256(SECRET));
    }

    // Refresh Token 생성 (7일)
    public String createRefreshToken(Long memberId) {
        return JWT.create()
                .withSubject(memberId.toString())
                .withIssuer(ISSUER)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .sign(Algorithm.HMAC256(SECRET));
    }

    // 토큰 검증
    public DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .withIssuer(ISSUER)
                .build()
                .verify(token);
    }
}