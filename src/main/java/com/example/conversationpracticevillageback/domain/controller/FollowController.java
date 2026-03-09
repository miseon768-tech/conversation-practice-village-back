package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Follow", description = "팔로우/언팔로우 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/follows")
@Validated
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우", description = "다른 사용자를 팔로우합니다.")
    @ApiResponse(responseCode = "200", description = "팔로우 성공")
    @PostMapping
    public void follow(@RequestParam Long followerId,
                       @RequestParam Long followingId) {
        followService.follow(followerId, followingId);
    }

    @Operation(summary = "언팔로우", description = "팔로우 중인 사용자를 언팔로우합니다.")
    @ApiResponse(responseCode = "200", description = "언팔로우 성공")
    @DeleteMapping
    public void unfollow(@RequestParam Long followerId,
                         @RequestParam Long followingId) {
        followService.unfollow(followerId, followingId);
    }
}
