package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.repository.MemberRepository;
import com.example.conversationpracticevillageback.domain.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final MemberRepository memberRepository;

    // 페르소나 생성
    public Persona create(Long memberId, Persona persona) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member not found"));

        persona.setMember(member);
        return personaRepository.save(persona);
    }

    // 페르소나 조회
    public Persona get(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("persona not found"));
    }

    // 특정 회원이 생성한 페르소나 리스트 조회
    public List<Persona> getByMember(Long memberId) {
        return personaRepository.findByMemberId(memberId);
    }

    // 페르소나 삭제
    public void delete(Long id) {
        personaRepository.deleteById(id);
    }
}
