package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.AI.AiService;
import com.example.conversationpracticevillageback.domain.dto.MessageDto;
import com.example.conversationpracticevillageback.domain.dto.response.ChatResponse;
import com.example.conversationpracticevillageback.domain.entity.Conversation;
import com.example.conversationpracticevillageback.domain.entity.Message;
import com.example.conversationpracticevillageback.domain.repository.ConversationRepository;
import com.example.conversationpracticevillageback.domain.repository.MessageRepository;
import com.example.conversationpracticevillageback.domain.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final PersonaRepository personaRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final AiService aiService;

    // 특정 대화방에서 메시지 전송 및 AI 응답 처리
    @Transactional
    public ChatResponse sendMessage(Long conversationId, String content) {
        log.info("메시지 전송 시작: conversationId={}, content={}", conversationId, content);

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> {
                    log.error("대화방을 찾을 수 없음: {}", conversationId);
                    return new RuntimeException("Conversation session not found");
                });

        log.info("대화방 조회 완료: persona={}", conversation.getPersona().getName());

        // 사용자 메시지 저장
        Message userMsg = Message.userMessage(conversation, content);
        messageRepository.save(userMsg);
        log.info("사용자 메시지 저장 완료: {}", content);

        // AI 응답 생성
        String aiReply = aiService.generateReply(conversation.getPersona(), content);
        log.info("AI 응답 생성 완료: {}", aiReply);

        // AI 응답 저장
        Message aiMsg = Message.aiMessage(conversation, aiReply);
        messageRepository.save(aiMsg);
        log.info("AI 메시지 저장 완료");

        return new ChatResponse(aiReply);
    }

    // 특정 대화방의 메시지 내역 조회
    @Transactional(readOnly = true)
    public List<MessageDto> getMessagesByConversation(Long conversationId) {
        return messageRepository
                .findByConversation_Persona_IdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(MessageDto::from)
                .toList();
    }
}