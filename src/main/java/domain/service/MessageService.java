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

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final PersonaRepository personaRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final AiService aiService;

    public ChatResponse chat(Long personaId, String userMessage) {

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

        Message userMsg = Message.builder()
                .conversation(conversation)
                .senderType(SenderType.USER)
                .content(userMessage)
                .build();

        messageRepository.save(userMsg);

        String aiReply = aiService.generateReply(persona, userMessage);

        Message aiMsg = Message.builder()
                .conversation(conversation)
                .senderType(SenderType.AI)
                .content(aiReply)
                .build();

        messageRepository.save(aiMsg);

        return new ChatResponse(aiReply);
    }

    public List<MessageDto> getMessages(Long personaId) {
        return messageRepository
                .findByConversation_Persona_IdOrderByCreatedAtAsc(personaId)
                .stream()
                .map(MessageDto::from)
                .toList();
    }
}