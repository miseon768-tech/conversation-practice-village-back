package com.example.conversationpracticevillageback.domain.dto.request;

import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "페르소나 생성 요청")
@Getter
@Setter
public class PersonaRequest {

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

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

    @JsonProperty("relationship_type")
    @Schema(description = "관계 유형", example = "친구")
    private String relationshipType;

    @JsonProperty("personality_keywords")
    @Schema(description = "성격 키워드", example = "활발함, 친절함")
    private String personalityKeywords;

    @JsonProperty("speech_style")
    @Schema(description = "말투", example = "존댓말")
    private String speechStyle;

    @Schema(description = "친밀도 스코어", example = "0")
    private Integer intimacyScore = 0;

    @Schema(description = "신뢰도 스코어", example = "0")
    private Integer trustScore = 0;

    // ...existing code...
    public Persona toEntity() {
        Persona persona = new Persona();
        persona.setNpcId(this.npcId);
        persona.setName(this.name);
        persona.setAge(this.age);
        persona.setJob(this.job);
        persona.setMbti(this.mbti);
        persona.setRelationshipType(this.relationshipType);
        persona.setPersonalityKeywords(this.personalityKeywords);
        persona.setSpeechStyle(this.speechStyle);
        persona.setIntimacyScore(this.intimacyScore);
        persona.setTrustScore(this.trustScore);

        return persona;
    }
}