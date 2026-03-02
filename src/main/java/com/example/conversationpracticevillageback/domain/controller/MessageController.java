package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.dto.MessageDto;
import com.example.conversationpracticevillageback.domain.dto.request.ChatRequest;
import com.example.conversationpracticevillageback.domain.dto.response.ChatResponse;
import com.example.conversationpracticevillageback.domain.service.MessageService;
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

    // 특정 대화방에 메시지 전송 (AI 답변 포함)
    @PostMapping("/{conversationId}")
    public ChatResponse sendMessage(@PathVariable Long conversationId,
                                    @RequestBody ChatRequest request) {
        return messageService.sendMessage(conversationId, request.getMessage());
    }

    // 특정 대화방의 모든 메시지 내역 조회
    @GetMapping("/{conversationId}")
    public List<MessageDto> getMessages(@PathVariable Long conversationId) {
        return messageService.getMessagesByConversation(conversationId);
    }
}
