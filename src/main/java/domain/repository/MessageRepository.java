package domain.repository;

import domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    Collection<Message> findByConversation_Persona_IdOrderByCreatedAtAsc(Long personaId);

    Collection<Message> findByConversation_IdOrderByCreatedAtAsc(Long conversationId);
}
