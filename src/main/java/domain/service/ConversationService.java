package domain.service;

import domain.AI.AiService;
import domain.dto.MessageDto;
import domain.dto.response.ChatResponse;
import domain.entity.Conversation;
import domain.entity.Message;
import domain.entity.Persona;
import domain.entity.SenderType;
import domain.repository.ConversationRepository;
import domain.repository.MessageRepository;
import domain.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final PersonaRepository personaRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final AiService aiService;

    @Transactional
    public ChatResponse chat(Long personaId, String userMessage) {

        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("persona not found"));

        // 1️⃣ 대화방 생성 (처음이면)
        Conversation conversation = Conversation.builder()
                .persona(persona)
                .build();

        conversationRepository.save(conversation);

        // 2️⃣ 유저 메시지 저장
        Message userMsg = Message.builder()
                .conversation(conversation)
                .senderType(SenderType.USER)
                .content(userMessage)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(userMsg);

        // 3️⃣ AI 응답 생성
        String aiReply = aiService.generateReply(persona, userMessage);

        // 4️⃣ AI 메시지 저장
        Message aiMsg = Message.builder()
                .conversation(conversation)
                .senderType(SenderType.AI)
                .content(aiReply)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(aiMsg);

        return new ChatResponse(aiReply);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(Long conversationId) {
        return messageRepository.findByConversation_IdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(MessageDto::from)
                .toList();
    }
}