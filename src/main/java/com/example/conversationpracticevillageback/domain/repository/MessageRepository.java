package com.example.conversationpracticevillageback.domain.repository;

import com.example.conversationpracticevillageback.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    Collection<Message> findByConversation_Persona_IdOrderByCreatedAtAsc(Long personaId);

}
