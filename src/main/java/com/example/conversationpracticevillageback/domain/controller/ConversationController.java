package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.entity.Conversation;
import com.example.conversationpracticevillageback.domain.service.ConversationService;
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

    // personaId로 기존 대화방을 찾거나, 없으면 새로 생성
    @PostMapping("/persona/{personaId}")
    public Long getOrCreateConversation(@PathVariable Long personaId) {
        return conversationService.getOrCreateConversation(personaId);
    }

    // 특정 페르소나와 나누었던 방 목록 조회
    @GetMapping("/persona/{personaId}")
    public List<Conversation> getConversationList(@PathVariable Long personaId) {
        return conversationService.getConversationsByPersona(personaId);
    }
}
