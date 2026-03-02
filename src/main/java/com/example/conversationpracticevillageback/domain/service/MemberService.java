package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        // 1. 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        // 2. 비밀번호 확인 (지금은 암호화 없이 생짜 비교, 나중에 BCrypt 적용)
        if (!member.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
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
