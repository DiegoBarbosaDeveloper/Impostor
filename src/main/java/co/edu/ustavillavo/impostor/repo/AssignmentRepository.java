package co.edu.ustavillavo.impostor.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.ustavillavo.impostor.entity.AssignmentEntity;
import co.edu.ustavillavo.impostor.entity.RoomEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, UUID> {
	void deleteByRoom(RoomEntity room);
	Optional<AssignmentEntity> findByRoom_CodeAndPlayer_PlayerId(String code, UUID playerId);
	List<AssignmentEntity> findByRoom_Code(String code);
}
