package co.edu.ustavillavo.impostor.service.assignment;

import co.edu.ustavillavo.impostor.domain.dto.Assignment;
import co.edu.ustavillavo.impostor.domain.entity.AssignmentEntity;
import co.edu.ustavillavo.impostor.repo.AssigmentRepository;
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
public class AssignmentServiceImpl implements AssigmentService {

    /*
        Dependencia básica del repositorio de asignación

        Los repos nos permiten interactuar con la BD
     */
    private final AssigmentRepository assigmentRepository;

    // Dependencia extra del repositorio de asignación del cuarto (Room)
    private final RoomRepository roomRepository;

    // Dependencia extra del repo para asignación de los jugadores (Player)
    private final PlayerRepository playerRepository;

    /*
        GET
     */

    // Obtienes una lista de entidades -> la conviertes a dto -> la regresas en lista
    @Override
    public List<Assignment> getAllAssignments() {
        return assigmentRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Obtienes una entidad -> la conviertes en dto -> y la devuelves en una caja
    @Override
    public Optional<Assignment> getAssignment(@NonNull UUID id) {

        return Optional.of(toDto(assigmentRepository.getReferenceById(id)));

    }


    /*
        POST
        Pasas dto -> guardas en bd -> regresas el dto
     */
    @Override
    public Optional<Assignment> saveAssignment(@NonNull Assignment dto) {

        assigmentRepository.save(toEntity(dto));

        return Optional.of(dto);
    }

    /*
        PUT

        Cambias el dato entero menos el identificador (ID)

        hallas la entidad por el ID -> cambias los valores (Nada nullo) -> guardas
     */
    @Override
    public void updateAssignment(@NonNull Assignment dto) {
        if (dto.id() == null){
            throw new RuntimeException("No Id for Updating Assignment");
        }

        if(dto.playerId() == null ||dto.roomId() == null || dto.word().isBlank() || dto.role() == null){
            throw new RuntimeException("Null Attributes Are Not Allowed to Update");
        }

       AssignmentEntity entity = assigmentRepository.getReferenceById(dto.id());

        entity.setRoom(roomRepository.getReferenceById(dto.roomId()));
        entity.setRole(dto.role());
        entity.setWord(dto.word());
        entity.setPlayer(playerRepository.getReferenceById(dto.playerId()));

        assigmentRepository.save(entity);
    }

    /*
        PATCH
     */
    @Override
    public void modifyAssignment(@NonNull Assignment dto) {
        if (dto.id() == null){
            throw new RuntimeException("No Id for Modifying Assignment");
        }
        AssignmentEntity entity = assigmentRepository.getReferenceById(dto.id());

        if(dto.playerId() != null){
            entity.setPlayer(playerRepository.getReferenceById(dto.playerId()));
        }
        if(dto.roomId() != null){
            entity.setRoom(roomRepository.getReferenceById(dto.roomId()));
        }
        if(!dto.word().isBlank()){
            entity.setWord(dto.word());
        }
        if(dto.role() != null){
            entity.setRole(dto.role());
        }
        assigmentRepository.save(entity);
    }

    /*
        DELETE
     */
    @Override
    public void deleteAssignment(@NonNull UUID id) {
        assigmentRepository.deleteById(id);
    }

    /*
        Métodos de Mapping o de transformación,
        estos métodos intercambian información entre entidad y DTO.
     */

    // DTO --> Entity
    private AssignmentEntity toEntity(Assignment dto){
        return new AssignmentEntity(
                dto.id(),
                roomRepository.getReferenceById(dto.roomId()),
                playerRepository.getReferenceById(dto.playerId()),
                dto.role(),
                dto.word()
        );
    }

    // Entity --> DTO
    private Assignment toDto(AssignmentEntity entity){
        return new Assignment(
                entity.getAssignmentId(),
                entity.getAssignmentId(),
                entity.getRoom().getRoomId(),
                entity.getRole(),
                entity.getWord()
        );
    }

}
