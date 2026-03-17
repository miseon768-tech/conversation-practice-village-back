package com.example.conversationpracticevillageback.domain.response;

public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long id;
    private String nickname;

    public LoginResponse(String accessToken, String refreshToken, Long id, String nickname) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.nickname = nickname;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}