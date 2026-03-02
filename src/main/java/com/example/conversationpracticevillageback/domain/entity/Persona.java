package com.example.conversationpracticevillageback.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @Column(name = "npc_id", unique = true, nullable = false)
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

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
