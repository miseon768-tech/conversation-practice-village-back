package com.example.conversationpracticevillageback.domain.repository;

import com.example.conversationpracticevillageback.domain.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona,Long> {
    List<Persona> findByMemberId(Long memberId);

    boolean existsByNpcId(String npcId);
}
