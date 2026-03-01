package domain.controller;

import domain.dto.MessageDto;
import domain.dto.request.ChatRequest;
import domain.dto.response.ChatResponse;
import domain.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Asset Price Stream", description = "자산 평가금액 스트림 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Validated
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/persona/{personaId}")
    public ChatResponse sendMessage(
            @PathVariable Long personaId,
            @RequestBody ChatRequest request
    ) {
        return messageService.chat(personaId, request.getMessage());
    }

    @GetMapping("/persona/{personaId}")
    public List<MessageDto> getMessages(@PathVariable Long personaId) {
        return messageService.getMessages(personaId);
    }
}
