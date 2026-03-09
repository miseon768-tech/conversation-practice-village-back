package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.entity.Conversation;
import com.example.conversationpracticevillageback.domain.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Conversation", description = "대화방 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/conversations")
@Validated
public class ConversationController {

    private final ConversationService conversationService;

    @Operation(summary = "대화방 조회 또는 생성", description = "페르소나와의 대화방을 조회하거나 없으면 새로 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대화방 ID 반환"),
            @ApiResponse(responseCode = "404", description = "페르소나를 찾을 수 없음")
    })
    @PostMapping("/persona/{personaId}")
    public Long getOrCreateConversation(@PathVariable Long personaId) {
        return conversationService.getOrCreateConversation(personaId);
    }

    @Operation(summary = "대화방 목록 조회", description = "특정 페르소나와 나눈 모든 대화방을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/persona/{personaId}")
    public List<Conversation> getConversationList(@PathVariable Long personaId) {
        return conversationService.getConversationsByPersona(personaId);
    }
}
