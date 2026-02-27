package co.edu.ustavillavo.impostor.domain.response;

import java.util.List;

import co.edu.ustavillavo.impostor.domain.enums.RoomStatus;
import lombok.Data;

@Data
public class CodeResponse {
    private RoomStatus status;
    private String category;
    private Byte currentRound;
    private List<CodePlayerResponse> players;
}
