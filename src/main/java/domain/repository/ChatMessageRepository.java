package domain.repository;

import domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<Follow,Long> {
}
