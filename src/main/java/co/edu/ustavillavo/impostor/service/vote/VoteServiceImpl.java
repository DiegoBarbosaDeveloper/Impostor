package co.edu.ustavillavo.impostor.service.vote;

import co.edu.ustavillavo.impostor.domain.dto.Vote;
import co.edu.ustavillavo.impostor.entity.VoteEntity;
import co.edu.ustavillavo.impostor.repo.PlayerRepository;
import co.edu.ustavillavo.impostor.repo.RoomRepository;
import co.edu.ustavillavo.impostor.repo.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;

    @Override
    public List<Vote> getAllVotes() {
        return voteRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Optional<Vote> getVote(@NonNull UUID id) {
        return Optional.of(toDto(voteRepository.getReferenceById(id)));
    }

    @Override
    public Optional<Vote> saveVote(@NonNull Vote dto) {
        if (dto.roomId() == null || dto.voterId() == null || dto.roundNumber() < 1) {
            throw new RuntimeException("Null or Invalid Attributes Not Allowed");
        }

        VoteEntity entity = new VoteEntity(
                UUID.randomUUID(),
                roomRepository.getReferenceById(dto.roomId()),
                dto.roundNumber(),
                playerRepository.getReferenceById(dto.voterId()),
                dto.votedId() != null ? playerRepository.getReferenceById(dto.votedId()) : null
        );

        voteRepository.save(entity);

        return Optional.of(dto);
    }

    @Override
    public void updateVote(@NonNull Vote dto) {
        if (dto.roomId() == null || dto.voterId() == null || dto.roundNumber() < 1) {
            throw new RuntimeException("Null or Invalid Attributes Not Allowed for Update");
        }

        // Dado que el registro Vote no tiene ID, no podemos actualizar
        // Este método necesitaría que el DTO Vote incluya un campo ID
        throw new RuntimeException("Update operation requires Vote ID");
    }

    @Override
    public void modifyVote(@NonNull Vote dto) {
        // Dado que el registro Vote no tiene ID, no podemos modificar
        // Este método necesitaría que el DTO Vote incluya un campo ID
        throw new RuntimeException("Modify operation requires Vote ID");
    }

    @Override
    public void deleteVote(@NonNull UUID id) {
        voteRepository.deleteById(id);
    }

    // Mapeadores
    private Vote toDto(VoteEntity entity) {
        return new Vote(
                entity.getVoteId(),
                entity.getRoom().getRoomId(),
                entity.getRoundNumber(),
                entity.getVoterPlayer().getPlayerId(),
                entity.getVotedPlayer() != null ? entity.getVotedPlayer().getPlayerId() : null
        );
    }

    private VoteEntity toEntity(Vote dto) {
        return new VoteEntity(
                dto.id(),
                roomRepository.getReferenceById(dto.roomId()),
                dto.roundNumber(),
                playerRepository.getReferenceById(dto.voterId()),
                dto.votedId() != null ? playerRepository.getReferenceById(dto.votedId()) : null
        );
    }
}
