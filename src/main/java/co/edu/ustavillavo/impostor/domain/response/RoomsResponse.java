package co.edu.ustavillavo.impostor.domain.response;

import java.util.UUID;

import lombok.Data;

@Data
public class RoomsResponse {
    private String roomCode;
    private UUID hostPlayerId;
}
