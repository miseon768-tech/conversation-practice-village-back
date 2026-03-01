package domain.controller;

import domain.dto.MessageDto;
import domain.dto.request.ChatRequest;
import domain.dto.response.ChatResponse;
import domain.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Asset Price Stream", description = "자산 평가금액 스트림 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/conversations")
@Validated
public class ConversationController {

    private final ConversationService conversationService;

    // 특정 페르소나와 새로운 대화 세션을 시작하고 메시지를 전송
    @PostMapping("/{personaId}")
    public ChatResponse chat(@PathVariable Long personaId,
                             @RequestBody ChatRequest request) {
        return conversationService.chat(personaId, request.getMessage());
    }

    // 특정 대화 내의 모든 메시지를 조회
    @GetMapping("/{conversationId}/messages")
    public List<MessageDto> getMessages(@PathVariable Long conversationId) {
        return conversationService.getMessages(conversationId);
    }
}
