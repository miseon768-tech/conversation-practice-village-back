package com.example.conversationpracticevillageback.domain.repository;

import com.example.conversationpracticevillageback.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
