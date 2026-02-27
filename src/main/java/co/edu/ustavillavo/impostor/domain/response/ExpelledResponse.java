package co.edu.ustavillavo.impostor.domain.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ExpelledResponse {
    private UUID id;
    private String nickname;
    private boolean wasImpostor;
}
