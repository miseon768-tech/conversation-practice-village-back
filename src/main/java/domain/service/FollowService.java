package domain.service;

import domain.entity.Follow;
import domain.entity.Member;
import domain.repository.FollowRepository;
import domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public void follow(Long followerId, Long followingId) {

        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("follower not found"));

        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("following not found"));

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);
    }

    public void unfollow(Long followerId, Long followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }
}
