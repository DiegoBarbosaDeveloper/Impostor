package co.edu.ustavillavo.impostor.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.ustavillavo.impostor.entity.RoomEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {
	Optional<RoomEntity> findByCode(String code);
	boolean existsByCode(String code);
}
