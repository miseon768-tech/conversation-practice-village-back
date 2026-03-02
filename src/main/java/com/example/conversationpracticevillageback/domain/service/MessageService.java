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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation session not found"));


        Message userMsg = Message.userMessage(conversation, content);
        messageRepository.save(userMsg);

        String aiReply = aiService.generateReply(conversation.getPersona(), content);

        Message aiMsg = Message.aiMessage(conversation, aiReply);
        messageRepository.save(aiMsg);

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