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

    // 회원 가입
    @PostMapping
    public Member create(@RequestBody Member member) {
        return memberService.create(member);
    }

    // 회원 정보 수정
    @GetMapping("/{memberId}")
    public Member get(@PathVariable Long memberId) {
        return memberService.get(memberId);
    }

    // 전체 회원 조회
    @GetMapping
    public List<Member> getAll() {
        return memberService.getAll();
    }

    // 회원 삭제
    @DeleteMapping("/{memberId}")
    public void delete(@PathVariable Long memberId) {
        memberService.delete(memberId);
    }
}

