package co.edu.ustavillavo.impostor.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.ustavillavo.entity.AssignmentEntity;

import java.util.UUID;

@Repository
public interface AssigmentRepository extends JpaRepository<AssignmentEntity, UUID> {
}
