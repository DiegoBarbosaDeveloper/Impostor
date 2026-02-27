package co.edu.ustavillavo.impostor.domain.response;

import java.util.UUID;

import lombok.Data;

@Data
public class PlayerResponse {
    private UUID playerId;
    private String nickname;
}
