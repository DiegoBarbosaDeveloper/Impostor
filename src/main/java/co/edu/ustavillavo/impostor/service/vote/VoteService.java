package co.edu.ustavillavo.impostor.service.vote;



import co.edu.ustavillavo.impostor.domain.dto.Vote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteService {

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
    List<Vote> getAllVotes();
    Optional<Vote> getVote(UUID id);

    /*
        POST
     */
    Optional<Vote> saveVote(Vote dto);

    /*
        PUT
     */
    void updateVote(Vote dto);

    /*
        PATCH
     */
    void modifyVote(Vote dto);

    /*
        DELETE
     */
    void deleteVote(UUID id);


}
