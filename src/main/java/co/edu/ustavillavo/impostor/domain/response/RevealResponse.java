package co.edu.ustavillavo.impostor.domain.response;

import lombok.Data;

import java.util.UUID;

@Data
public class RevealResponse {
    private UUID playerId;
    private String nickname;
    private String role;
}
