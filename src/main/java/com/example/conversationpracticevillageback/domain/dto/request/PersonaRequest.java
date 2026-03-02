package com.example.conversationpracticevillageback.domain.dto.request;

import com.example.conversationpracticevillageback.domain.entity.Persona;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaRequest {
    private Long memberId;
    private String npcId;
    private String name;
    private Integer age;
    private String job;
    private String mbti;
    private String relationshipType;
    private String personalityKeywords;
    private String speechStyle;
    private Integer intimacyScore = 0;
    private Integer trustScore = 0;

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