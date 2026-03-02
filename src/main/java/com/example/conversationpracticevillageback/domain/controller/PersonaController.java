package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.dto.request.PersonaRequest;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Asset Price Stream", description = "자산 평가금액 스트림 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/personas")
@Validated
public class PersonaController {

    private final PersonaService personaService;


    // 페르소나 생성
    @PostMapping
    public ResponseEntity<Persona> create(@RequestBody PersonaRequest request) {
        Persona created = personaService.create(request.getMemberId(), request.getNpcId(), request.toEntity());
        return ResponseEntity.ok(created);
    }

    // 페르소나 조회
    @GetMapping("/{personaId}")
    public Persona get(@PathVariable Long personaId) {
        return personaService.get(personaId);
    }

    // 회원의 모든 페르소나 조회
    @GetMapping("/member/{memberId}")
    public List<Persona> getByMember(@PathVariable Long memberId) {
        return personaService.getByMember(memberId);
    }

    // 페르소나 삭제
    @DeleteMapping("/{personaId}")
    public void delete(@PathVariable Long personaId) {
        personaService.delete(personaId);
    }
}
