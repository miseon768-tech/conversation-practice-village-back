package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.repository.MemberRepository;
import com.example.conversationpracticevillageback.domain.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final MemberRepository memberRepository;

    // 페르소나 생성
    @Transactional
    public Persona create(Long memberId, String npcId, Persona persona) {

        if (personaRepository.existsByNpcId(npcId)) {
            throw new RuntimeException("이미 인격이 부여된 캐릭터입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member not found"));

        persona.setMember(member);
        persona.setNpcId(npcId);

        return personaRepository.save(persona);
    }

    // 페르소나 조회
    @Transactional(readOnly = true)
    public Persona get(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("persona not found"));
    }

    // 특정 회원이 생성한 페르소나 리스트 조회
    @Transactional(readOnly = true)
    public List<Persona> getByMember(Long memberId) {
        return personaRepository.findByMemberId(memberId);
    }

    // 페르소나 삭제
    @Transactional
    public void delete(Long id) {
        personaRepository.deleteById(id);
    }
}
