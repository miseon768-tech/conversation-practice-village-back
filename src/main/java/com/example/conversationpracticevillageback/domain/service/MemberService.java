package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    public Member create(Member member) {
        return memberRepository.save(member);
    }

    // 로그인
    public Member login(String email, String password) {
        final String authFailMessage = "이메일 또는 비밀번호가 일치하지 않습니다.";

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, authFailMessage));

        // TODO: BCrypt 적용 전까지는 평문 비교
        if (member.getPassword() == null || !member.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, authFailMessage);
        }

        return member;
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
