package co.edu.ustavillavo.impostor.service.assignment;

import co.edu.ustavillavo.impostor.domain.dto.Assignment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssigmentService {


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
    List<Assignment> getAllAssignments();
    Optional<Assignment> getAssignment(UUID id);

    /*
        POST
     */
    Assignment saveAssignment(Assignment dto);

    /*
        PUT
     */
    void updateAssignment(Assignment dto);

    /*
        PATCH
     */
    void modifyAssignment(Assignment dto);
    /*
        DELETE
     */
    void deleteAssignment(UUID id);

}
