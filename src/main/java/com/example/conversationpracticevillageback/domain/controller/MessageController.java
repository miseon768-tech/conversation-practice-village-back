package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.response.MessageDto;
import com.example.conversationpracticevillageback.domain.request.ChatRequest;
import com.example.conversationpracticevillageback.domain.response.ChatResponse;
import com.example.conversationpracticevillageback.domain.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Message", description = "메시지 및 AI 응답 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Validated
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "메시지 전송", description = "대화방에 메시지를 전송하고 AI 응답을 받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 전송 및 AI 응답 성공",
                    content = @Content(schema = @Schema(implementation = ChatResponse.class))),
            @ApiResponse(responseCode = "404", description = "대화방을 찾을 수 없음")
    })
    @PostMapping("/{conversationId}")
    public ChatResponse sendMessage(@PathVariable Long conversationId,
                                    @RequestBody ChatRequest request) {
        return messageService.sendMessage(conversationId, request.getMessage());
    }

    @Operation(summary = "대화 내역 조회", description = "특정 대화방의 모든 메시지 내역을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{conversationId}")
    public List<MessageDto> getMessages(@PathVariable Long conversationId) {
        return messageService.getMessagesByConversation(conversationId);
    }
}
