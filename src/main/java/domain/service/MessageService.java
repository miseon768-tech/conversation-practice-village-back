package domain.service;

import domain.AI.AiService;
import domain.entity.Message;
import domain.entity.Persona;
import domain.repository.MessageRepository;
import domain.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final PersonaRepository personaRepository;
    private final MessageRepository messageRepository;
    private final AiService aiService;

    public ChatResponse chat(Long personaId, String userMessage) {

        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("persona not found"));

        Message userMsg = new Message(persona, "user", userMessage);
        messageRepository.save(userMsg);

        String aiReply = aiService.generateReply(persona, userMessage);

        Message aiMsg = new Message(persona, "ai", aiReply);
        messageRepository.save(aiMsg);

        return new ChatResponse(aiReply);
    }

    public List<MessageDto> getMessages(Long personaId) {
        return messageRepository.findByPersonaIdOrderByCreatedAtAsc(personaId)
                .stream()
                .map(MessageDto::from)
                .collect(Collectors.toList());
    }
}
