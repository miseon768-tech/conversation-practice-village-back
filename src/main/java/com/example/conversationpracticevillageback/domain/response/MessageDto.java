package com.example.conversationpracticevillageback.domain.response;

import com.example.conversationpracticevillageback.domain.entity.Message;
import com.example.conversationpracticevillageback.domain.entity.SenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "메시지 정보")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    @Schema(description = "메시지 ID", example = "1")
    private Long id;

    @Schema(description = "메시지 내용", example = "안녕하세요!")
    private String content;

    @Schema(description = "발신자 타입 (USER/AI)", example = "USER")
    private SenderType senderType;

    @Schema(description = "메시지 생성 시간", example = "2026-03-10T10:00:00")
    private LocalDateTime createdAt;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderType(message.getSenderType())
                .createdAt(message.getCreatedAt())
                .build();
    }
}