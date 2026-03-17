package com.example.conversationpracticevillageback.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "로그인 요청")
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @Schema(description = "회원 이메일", example = "user@example.com")
    private String email;

    @Schema(description = "회원 비밀번호", example = "password123")
    private String password;
}