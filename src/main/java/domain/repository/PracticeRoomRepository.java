package domain.repository;

import domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PracticeRoomRepository extends JpaRepository<Follow,Long> {
}
