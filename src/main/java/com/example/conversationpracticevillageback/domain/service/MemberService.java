package com.example.conversationpracticevillageback.domain.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.conversationpracticevillageback.domain.response.LoginResponse;
import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.repository.MemberRepository;
import com.example.conversationpracticevillageback.global.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;


    // 회원 가입
    public Member create(Member member) {
        return memberRepository.save(member);
    }

    // 로그인
    public LoginResponse login(String email, String password) {
        final String authFailMessage = "이메일 또는 비밀번호가 일치하지 않습니다.";

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, authFailMessage));

        // TODO: BCrypt 적용 전까지는 평문 비교
        if (member.getPassword() == null || !member.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, authFailMessage);
        }

        String accessToken = jwtUtil.createAccessToken(member.getId());
        String refreshToken = jwtUtil.createRefreshToken(member.getId());

        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        return new LoginResponse(accessToken, refreshToken, member.getId(), member.getNickname());
    }

    // AccessToken 재발급
    public LoginResponse refreshAccessToken(String refreshToken) {
        DecodedJWT decoded;
        try {
            decoded = jwtUtil.verifyToken(refreshToken);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token 유효하지 않음");
        }

        Long memberId = Long.valueOf(decoded.getSubject());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자 없음"));

        if (!refreshToken.equals(member.getRefreshToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token 불일치");
        }

        String newAccessToken = jwtUtil.createAccessToken(memberId);
        String newRefreshToken = jwtUtil.createRefreshToken(memberId);

        member.setRefreshToken(newRefreshToken);
        memberRepository.save(member);

        return new LoginResponse(newAccessToken, newRefreshToken, member.getId(), member.getNickname());
    }

    // 회원 정보 조회
    public Member get(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("member not found"));
    }

    // 모든 회원 조회
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    // 회원 삭제
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
