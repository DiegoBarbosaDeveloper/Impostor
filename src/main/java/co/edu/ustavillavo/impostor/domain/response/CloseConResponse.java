package co.edu.ustavillavo.impostor.domain.response;

import co.edu.ustavillavo.impostor.domain.dto.Player;
import co.edu.ustavillavo.impostor.domain.enums.RoomStatus;
import lombok.Data;

@Data
public class CloseConResponse {
    private Byte roundClosed;
    private Player expelled;
    private RoomStatus status;
    private Byte nextRound;
    private Byte aliveCount;
}
