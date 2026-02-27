package co.edu.ustavillavo.impostor.domain.response;

import co.edu.ustavillavo.impostor.domain.enums.RoomStatus;
import lombok.Data;

@Data
public class CloseConResponse {
    private Byte roundClosed;
    private ExpelledResponse expelled;
    private RoomStatus status;
    private Byte nextRound;
    private Integer aliveCount;
}
