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

    public Persona create(Long memberId, Persona persona) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("member not found"));

        persona.setMember(member);
        return personaRepository.save(persona);
    }

    public Persona get(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("persona not found"));
    }

    public List<Persona> getByMember(Long memberId) {
        return personaRepository.findByMemberId(memberId);
    }

    public void delete(Long id) {
        personaRepository.deleteById(id);
    }
}
