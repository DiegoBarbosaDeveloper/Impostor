package co.edu.ustavillavo.impostor.service.room;

import co.edu.ustavillavo.impostor.domain.dto.Room;
import co.edu.ustavillavo.impostor.domain.entity.RoomEntity;
import co.edu.ustavillavo.impostor.repo.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<Room> getAllRooms() {
        return List.of();
    }

    @Override
    public Optional<Room> getRoom(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Room> saveRoom(Room dto) {
        return Optional.empty();
    }

    @Override
    public void updateRoom(Room dto) {

    }

    @Override
    public void modifyRoom(Room dto) {

    }

    @Override
    public void deleteRoom(UUID id) {

    }

    private RoomEntity toEntity(){
        return null;
    }

    private Room toDto(){
        return null;
    }
}
