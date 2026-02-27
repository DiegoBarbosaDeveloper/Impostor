package co.edu.ustavillavo.impostor.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.ustavillavo.impostor.entity.VoteEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, UUID> {
	boolean existsByRoom_CodeAndRoundNumberAndVoterPlayer_PlayerId(String code, int roundNumber, UUID voterId);
	List<VoteEntity> findByRoom_CodeAndRoundNumber(String code, int roundNumber);
}
