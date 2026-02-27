package co.edu.ustavillavo.impostor.service.player;

import co.edu.ustavillavo.entity.PlayerEntity;
import co.edu.ustavillavo.impostor.domain.dto.Player;
import co.edu.ustavillavo.impostor.repo.PlayerRepository;
import co.edu.ustavillavo.impostor.repo.RoomRepository;
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
public class PlayerServiceImpl implements PlayerService{

    private final PlayerRepository playerRepository;

    private final RoomRepository roomRepository;

    @Override
    public List<Player> getAllPlayers() {
        return  playerRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public Optional<Player> getPlayer(@NonNull UUID id) {
        return Optional.of(toDto(playerRepository.getReferenceById(id)));
    }

    @Override
    public Player savePlayer(@NonNull Player dto) {
        if (dto.roomId() == null || dto.nickname().isBlank()){
            throw new RuntimeException("Null Attributes Not Allowed");
        }

        PlayerEntity entity = new PlayerEntity(
                dto.id(),
                roomRepository.getReferenceById(dto.roomId()),
                dto.nickname(),
                true
        );

        playerRepository.save(entity);


        return (dto);
    }

    @Override
    public void updatePlayer(@NonNull Player dto) {
        if (dto.id() == null){
            throw new RuntimeException("No Id for Updating Player");
        }

        if(dto.roomId() == null || dto.nickname() == null){
            throw new RuntimeException("Null Attributes Are Not Allowed for Update " +
                    "player");
        }

        PlayerEntity entity = playerRepository.getReferenceById(dto.id());

        entity.setRoom(roomRepository.getReferenceById(dto.roomId()));
        entity.setAlive(dto.alive());
        entity.setNickname(dto.nickname());

        playerRepository.save(entity);

    }

    @Override
    public void modifyPlayer(@NonNull Player dto) {
        if (dto.id() == null){
            throw new RuntimeException("No Id for Modifying Player");
        }

        PlayerEntity entity = playerRepository.getReferenceById(dto.id());

        if (dto.roomId() != null){
            entity.setRoom(roomRepository.getReferenceById(dto.roomId()));
        }

        if (!dto.nickname().isBlank()){
            entity.setNickname(dto.nickname());
        }

        if (entity.isAlive() != dto.alive()){
            entity.setAlive(!dto.alive());
        }

    }

    @Override
    public void deletePlayer(@NonNull UUID id) {
        playerRepository.deleteById(id);

    }



    // Mappers
    private Player toDto(PlayerEntity entity){
        return new Player(
                entity.getPlayerId(),
                entity.getRoom().getRoomId(),
                entity.getNickname(),
                entity.isAlive()
        );
    }

    private PlayerEntity toEntity(Player dto){
        return new PlayerEntity(
                dto.id(),
                roomRepository.getReferenceById(dto.roomId()),
                dto.nickname(),
                dto.alive()
        );
    }
}
