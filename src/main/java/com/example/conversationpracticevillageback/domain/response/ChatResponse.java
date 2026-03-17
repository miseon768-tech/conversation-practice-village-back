package com.example.conversationpracticevillageback.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "AI 응답 메시지")
@Getter
@AllArgsConstructor
public class ChatResponse {

    @Schema(description = "AI의 응답 내용", example = "안녕하세요! 뵙게 되어 반가워요.")
    private String reply;

}