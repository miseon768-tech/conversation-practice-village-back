package domain.controller;

import domain.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Asset Price Stream", description = "자산 평가금액 스트림 관련 API")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/follows")
@Validated
public class FollowController {


    private final FollowService followService;

    @PostMapping
    public void follow(
            @RequestParam Long followerId,
            @RequestParam Long followingId
    ) {
        followService.follow(followerId, followingId);
    }

    @DeleteMapping
    public void unfollow(
            @RequestParam Long followerId,
            @RequestParam Long followingId
    ) {
        followService.unfollow(followerId, followingId);
    }
}
