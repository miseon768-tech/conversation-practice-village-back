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

    @PostMapping("/member/{memberId}")
    public Persona create(
            @PathVariable Long memberId,
            @RequestBody Persona request
    ) {
        return personaService.create(memberId, request);
    }

    @GetMapping("/{id}")
    public Persona get(@PathVariable Long id) {
        return personaService.get(id);
    }

    @GetMapping("/member/{memberId}")
    public List<Persona> getByMember(@PathVariable Long memberId) {
        return personaService.getByMember(memberId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        personaService.delete(id);
    }
}
