package co.edu.ustavillavo.impostor.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.ustavillavo.impostor.entity.PlayerEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, UUID> {
	List<PlayerEntity> findByRoom_Code(String code);
	List<PlayerEntity> findByRoom_CodeAndAliveTrue(String code);
	Optional<PlayerEntity> findByRoom_CodeAndPlayerId(String code, UUID playerId);
}
