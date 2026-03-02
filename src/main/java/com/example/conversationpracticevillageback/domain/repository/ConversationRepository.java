package com.example.conversationpracticevillageback.domain.repository;

import com.example.conversationpracticevillageback.domain.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    List<Conversation> findByPersona_IdOrderByIdDesc(Long personaId);
}
