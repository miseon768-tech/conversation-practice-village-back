package domain.controller;

import domain.entity.Persona;
import domain.service.PersonaService;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/member/{memberId}")
    public Persona create(@PathVariable Long memberId,
                          @RequestBody Persona persona) {
        return personaService.create(memberId, persona);
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
