package co.edu.ustavillavo.impostor.domain.response;

import java.util.UUID;

import lombok.Data;

@Data
public class CodePlayerResponse {
    private UUID id;
    private String nickname;
    private boolean alive;
}
