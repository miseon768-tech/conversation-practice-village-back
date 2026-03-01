package domain.repository;

import domain.entity.Follow;
import domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
