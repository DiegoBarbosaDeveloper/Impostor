package co.edu.ustavillavo.impostor.repo;

import co.edu.ustavillavo.impostor.domain.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssigmentRepository extends JpaRepository<AssignmentEntity, UUID> {
}
