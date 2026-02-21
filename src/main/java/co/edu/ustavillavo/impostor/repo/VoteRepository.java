package co.edu.ustavillavo.impostor.repo;

import co.edu.ustavillavo.impostor.domain.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, UUID> {
}
