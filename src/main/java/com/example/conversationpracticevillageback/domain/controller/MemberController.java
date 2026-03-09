package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.dto.request.LoginRequest;
import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Validated
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원 가입", description = "새로운 회원을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입 성공",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public Member create(@RequestBody Member member) {
        return memberService.create(member);
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 이메일 또는 비밀번호가 일치하지 않음")
    })
    @PostMapping("/login")
    public Member login(@RequestBody LoginRequest loginRequest) {
        return memberService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @Operation(summary = "회원 정보 조회", description = "회원 ID로 회원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @GetMapping("/{memberId}")
    public Member get(@PathVariable Long memberId) {
        return memberService.get(memberId);
    }

    @Operation(summary = "전체 회원 조회", description = "전체 회원 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public List<Member> getAll() {
        return memberService.getAll();
    }

    @Operation(summary = "회원 삭제", description = "회원 ID로 회원을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @DeleteMapping("/{memberId}")
    public void delete(@PathVariable Long memberId) {
        memberService.delete(memberId);
    }
}

