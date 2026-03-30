package com.example.conversationpracticevillageback.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "채팅 메시지 요청")
@Getter
@AllArgsConstructor
public class ChatRequest {

    @Schema(description = "전송할 메시지 내용", example = "안녕하세요!")
    private String message;

}