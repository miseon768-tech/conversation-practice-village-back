package domain.controller;

import domain.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Asset Price Stream", description = "자산 평가금액 스트림 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/conversations")
@Validated
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/{personaId}")
    public ChatResponse chat(
            @PathVariable Long personaId,
            @RequestBody ChatRequest request
    ) {
        return conversationService.chat(personaId, request.getMessage());
    }

    @GetMapping("/{conversationId}/messages")
    public List<MessageDto> getMessages(@PathVariable Long conversationId) {
        return conversationService.getMessages(conversationId);
    }
}
