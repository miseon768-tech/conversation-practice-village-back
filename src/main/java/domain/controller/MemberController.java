package domain.controller;

import domain.entity.Member;
import domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Asset Price Stream", description = "자산 평가금액 스트림 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Validated
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public Member create(@RequestBody Member request) {
        return memberService.create(request);
    }

    @GetMapping("/{id}")
    public Member get(@PathVariable Long id) {
        return memberService.get(id);
    }

    @GetMapping
    public List<Member> getAll() {
        return memberService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }
}

