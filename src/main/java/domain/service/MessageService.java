package domain.service;

import domain.AI.AiService;
import domain.dto.MessageDto;
import domain.dto.response.ChatResponse;
import domain.entity.Conversation;
import domain.entity.Message;
import domain.entity.Persona;
import domain.repository.ConversationRepository;
import domain.repository.MessageRepository;
import domain.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final PersonaRepository personaRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final AiService aiService;

    // 사용자 메시지 저장 -> AI 답변 생성 -> AI 메시지 저장 -> 답변 반환
    public ChatResponse chat(Long personaId, String content) {

        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("persona not found"));

        Conversation conversation = conversationRepository
                .findTopByPersona_IdOrderByIdDesc(personaId)
                .orElseGet(() -> {
                    Conversation newConv = Conversation.builder()
                            .persona(persona)
                            .build();
                    return conversationRepository.save(newConv);
                });

        Message userMsg = Message.userMessage(conversation, content);
        messageRepository.save(userMsg);

        String aiReply = aiService.generateReply(persona, content);

        Message aiMsg = Message.aiMessage(conversation, aiReply);
        messageRepository.save(aiMsg);

        return new ChatResponse(aiReply);
    }

    // 특정 페르소나의 대화 내역 조회
    public List<MessageDto> getMessages(Long personaId) {
        return messageRepository
                .findByConversation_Persona_IdOrderByCreatedAtAsc(personaId)
                .stream()
                .map(MessageDto::from)
                .toList();
    }
}