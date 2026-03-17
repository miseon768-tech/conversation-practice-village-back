package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.request.PersonaRequest;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Persona", description = "NPC 페르소나 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/personas")
@Validated
public class PersonaController {

    private final PersonaService personaService;

    @Operation(summary = "페르소나 생성", description = "새로운 NPC 페르소나를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "페르소나 생성 성공",
                    content = @Content(schema = @Schema(implementation = Persona.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<Persona> create(@RequestBody PersonaRequest request) {
        Persona created = personaService.create(request.getMemberId(), request.getNpcId(), request.toEntity());
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "페르소나 조회", description = "페르소나 ID로 NPC 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Persona.class))),
            @ApiResponse(responseCode = "404", description = "페르소나를 찾을 수 없음")
    })
    @GetMapping("/{personaId}")
    public Persona get(@PathVariable Long personaId) {
        return personaService.get(personaId);
    }

    @Operation(summary = "회원별 페르소나 조회", description = "특정 회원의 모든 페르소나를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/member/{memberId}")
    public List<Persona> getByMember(@PathVariable Long memberId) {
        return personaService.getByMember(memberId);
    }

    @Operation(summary = "페르소나 삭제", description = "페르소나 ID로 NPC를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @DeleteMapping("/{personaId}")
    public void delete(@PathVariable Long personaId) {
        personaService.delete(personaId);
    }
}
