package com.example.conversationpracticevillageback.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "메시지 정보")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "메시지 ID", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    @Schema(description = "발신자 타입", example = "USER")
    private SenderType senderType;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "메시지 내용", example = "안녕하세요!")
    private String content;

    @Schema(description = "메시지 생성 시간")
    private LocalDateTime createdAt;

    public enum SenderType {
        USER, AI
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ...existing code...
    public static Message userMessage(Conversation conversation, String content) {
        return Message.builder()
                .conversation(conversation)
                .senderType(SenderType.USER)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Message aiMessage(Conversation conversation, String content) {
        return Message.builder()
                .conversation(conversation)
                .senderType(SenderType.AI)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
