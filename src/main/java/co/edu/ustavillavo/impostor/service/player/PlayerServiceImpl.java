package co.edu.ustavillavo.impostor.service.player;

import co.edu.ustavillavo.impostor.domain.dto.Player;
import co.edu.ustavillavo.impostor.repo.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService{

    private final PlayerRepository playerRepository;

    @Override
    public List<Player> getAllPlayers() {
        return List.of();
    }

    @Override
    public Optional<Player> getPlayer(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Player> savePlayer(Player dto) {
        return Optional.empty();
    }

    @Override
    public void updatePlayer(Player dto) {

    }

    @Override
    public void modifyPlayer(Player dto) {

    }

    @Override
    public void deletePlayer(UUID id) {

    }

}
