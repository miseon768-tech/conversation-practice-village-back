package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.entity.Conversation;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.repository.ConversationRepository;
import com.example.conversationpracticevillageback.domain.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final PersonaRepository personaRepository;
    private final ConversationRepository conversationRepository;

    // personaId로 기존 대화방을 찾거나, 없으면 새로 생성
    @Transactional
    public Long getOrCreateConversation(Long personaId) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("persona not found"));

        // 가장 최근의 대화방 조회
        List<Conversation> conversations = conversationRepository.findByPersona_IdOrderByIdDesc(personaId);

        if (!conversations.isEmpty()) {
            return conversations.get(0).getId();
        }

        // 대화방이 없으면 새로 생성
        Conversation conversation = Conversation.builder()
                .persona(persona)
                .build();

        return conversationRepository.save(conversation).getId();
    }

    // 특정 페르소나와 나누었던 대화방 리스트 조회
    @Transactional(readOnly = true)
    public List<Conversation> getConversationsByPersona(Long personaId) {
        return conversationRepository.findByPersona_IdOrderByIdDesc(personaId);
    }
}