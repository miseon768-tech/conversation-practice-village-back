package com.example.conversationpracticevillageback.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "회원 정보")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "회원 이메일", example = "user@example.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "회원 닉네임", example = "사용자명")
    private String nickname;

    @Schema(description = "회원 비밀번호")
    private String password;

    @Column(name = "refresh_token")
    @Schema(description = "회원 리프레시 토큰")
    private String refreshToken;

    @Schema(description = "가입 시간")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ...existing code...
    @OneToMany(mappedBy = "member")
    private List<Persona> personas = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<Follow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private List<Follow> followerList = new ArrayList<>();
}
