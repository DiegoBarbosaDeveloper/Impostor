package co.edu.ustavillavo.impostor.service.player;

import co.edu.ustavillavo.impostor.domain.dto.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerService {
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
    List<Player> getAllPlayers();
    Optional<Player> getPlayer(UUID id);

    /*
        POST
     */
    Optional<Player> savePlayer(Player dto);

    /*
        PUT
     */
    void updatePlayer(Player dto);

    /*
        PATCH
     */
    void modifyPlayer(Player dto);

    /*
        DELETE
     */
    void  deletePlayer(UUID id);

}
