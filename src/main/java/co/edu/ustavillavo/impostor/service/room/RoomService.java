package co.edu.ustavillavo.impostor.service.room;

import co.edu.ustavillavo.impostor.domain.dto.Room;
import co.edu.ustavillavo.impostor.domain.response.CodeResponse;
import co.edu.ustavillavo.impostor.domain.response.PlayerResponse;
import co.edu.ustavillavo.impostor.domain.response.RoleStatus;
import co.edu.ustavillavo.impostor.domain.response.RoomsResponse;
import co.edu.ustavillavo.impostor.domain.response.StartResponse;
import co.edu.ustavillavo.impostor.domain.response.VoteResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomService {

    /*
       PETICIONES HTTP/HTTPS

        GET --> Obtener a todos los datos && Obtener un dato
        POST --> Crear entidad o registro en la BD
        PUT --> Actualiza el dato
        PATCH --> Modifica partes del dato
        DELETE --> Eliminar el dato
     */


    /*
        GET
     */
    List<Room> getAllRooms();
    Optional<Room> getRoom(UUID id);

    /*
        POST
     */
    Room saveRoom(Room dto);
    RoomsResponse createRoomWithHost(String hostNickname, String category, Integer impostorCount);
    PlayerResponse joinPlayer(String code, String nickname);
    StartResponse startGame(String code, UUID hostPlayerId);
    RoleStatus getPersonalWord(String code, UUID playerId);
    VoteResponse registerVote(String code, UUID voterId, UUID votedId);
    Object closeRound(String code, UUID hostPlayerId);

    /*
        PUT
     */
    void updateRoom(Room dto);

    /*
        PATCH
     */
    void modifyRoom(Room dto);
    CodeResponse getRoomState(String code);
    /*
        DELETE
     */
    void deleteRoom(UUID id);

}
