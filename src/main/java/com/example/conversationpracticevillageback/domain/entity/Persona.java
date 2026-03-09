package com.example.conversationpracticevillageback.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Schema(description = "NPC 페르소나 정보")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "페르소나 ID", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @Column(name = "npc_id", unique = true, nullable = false)
    @Schema(description = "NPC ID (고유값)", example = "npc_001")
    private String npcId;

    @Schema(description = "NPC 이름", example = "영희")
    private String name;

    @Schema(description = "나이", example = "25")
    private Integer age;

    @Schema(description = "직업", example = "카페 알바생")
    private String job;

    @Schema(description = "MBTI", example = "ENFP")
    private String mbti;

    @Schema(description = "관계 유형", example = "친구")
    private String relationshipType;

    @Schema(description = "성격 키워드", example = "활발함, 친절함")
    private String personalityKeywords;

    @Schema(description = "말투", example = "존댓말")
    private String speechStyle;

    @Schema(description = "친밀도 스코어", example = "0")
    private Integer intimacyScore = 0;

    @Schema(description = "신뢰도 스코어", example = "0")
    private Integer trustScore = 0;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
