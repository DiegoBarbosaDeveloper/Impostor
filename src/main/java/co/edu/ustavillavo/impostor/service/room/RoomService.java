package co.edu.ustavillavo.impostor.service.room;

import co.edu.ustavillavo.impostor.domain.dto.Room;

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

    /*
        PUT
     */
    void updateRoom(Room dto);

    /*
        PATCH
     */
    void modifyRoom(Room dto);
    /*
        DELETE
     */
    void deleteRoom(UUID id);

}
