package co.edu.ustavillavo.impostor.service.room;

import co.edu.ustavillavo.impostor.domain.dto.Room;
import co.edu.ustavillavo.impostor.entity.RoomEntity;
import co.edu.ustavillavo.impostor.repo.PlayerRepository;
import co.edu.ustavillavo.impostor.repo.RoomRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public Optional<Room> getRoom(@NonNull UUID id) {
        return Optional.of(toDto(roomRepository.getReferenceById(id)));
    }

    @Override
    public Room saveRoom(@NonNull Room dto) {
        if (dto.code().isBlank() || dto.status() == null || dto.hostPlayerId() == null
        || dto.category().isBlank()  || dto.currentRound() == null
        || dto.secretWord() == null || dto.winnerTeam() == null){
            throw new RuntimeException("Null Attributes Not Allowed");
        }

        if (dto.impostorCount() == null || dto.impostorCount() < 1){
            RoomEntity entity = toEntity(dto);
            entity.setImpostorCount(1);

            roomRepository.save(entity);

            return dto;
        }

        roomRepository.save(toEntity(dto));

        return dto;

    }

    @Override
    public void updateRoom(@NonNull Room dto) {

        if(dto.id() == null){
            throw new RuntimeException("No Id for Updating Room");
        }

        if(dto.winnerTeam().isBlank() || dto.impostorCount()  == null|| dto.currentRound() == null ||
        dto.category().isBlank() || dto.status() == null || dto.hostPlayerId() == null){
            throw new RuntimeException("No Null attributes for Updating Room");
        }

        RoomEntity entity = roomRepository.getReferenceById(dto.id());

        entity.setCategory(dto.category());
        entity.setCode(dto.code());
        entity.setStatus(dto.status());
        entity.setHostPlayer(playerRepository.getReferenceById(dto.hostPlayerId()));
        entity.setCurrentRound(dto.currentRound());
        entity.setImpostorCount(dto.impostorCount());
        entity.setSecretWord(dto.secretWord());
        entity.setWinnerTeam(dto.winnerTeam());

        roomRepository.save(entity);

    }

    @Override
    public void modifyRoom(@NonNull Room dto) {

        if (dto.id() == null){
            throw new RuntimeException("No id for Modifying Room");
        }

        RoomEntity entity = roomRepository.getReferenceById(dto.id());

        if (!dto.code().isBlank()){
            entity.setCode(dto.code());
        }

        if (dto.status() != null){
            entity.setStatus(dto.status());
        }

        if (dto.hostPlayerId() != null){
            entity.setHostPlayer(playerRepository.getReferenceById(dto.hostPlayerId()));
        }

        if (!dto.category().isBlank()){
            entity.setCategory(dto.category());
        }

        if (dto.impostorCount() != null && dto.impostorCount() > 0){
            entity.setImpostorCount(dto.impostorCount());
        }

        if (dto.currentRound() != null){
            entity.setCurrentRound(dto.currentRound());
        }

        if (dto.secretWord() != null && !dto.secretWord().isBlank()){
            entity.setSecretWord(dto.secretWord());
        }

        if (!dto.winnerTeam().isBlank()){
            entity.setWinnerTeam(dto.winnerTeam());
        }

        roomRepository.save(entity);
    }

    @Override
    public void deleteRoom(UUID id) {
        roomRepository.deleteById(id);
    }

    private RoomEntity toEntity(Room dto){
        return new RoomEntity(
                dto.id(),
                dto.code(),
                dto.status(),
                playerRepository.getReferenceById(dto.hostPlayerId()),
                dto.category(),
                dto.impostorCount(),
                dto.currentRound(),
                dto.secretWord(),
                dto.winnerTeam()
        );
    }

    private Room toDto(RoomEntity entity){
        return new Room(
                entity.getRoomId(),
                entity.getCode(),
                entity.getStatus(),
                entity.getHostPlayer().getPlayerId(),
                entity.getCategory(),
                entity.getImpostorCount(),
                entity.getCurrentRound(),
                entity.getSecretWord(),
                entity.getWinnerTeam()
        );
    }
}
