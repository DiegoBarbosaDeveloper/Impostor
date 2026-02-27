package co.edu.ustavillavo.impostor.domain.response;

import co.edu.ustavillavo.impostor.domain.enums.RoomStatus;
import lombok.Data;

import java.util.List;

@Data
public class CloseEndResponse {
    private Byte roundClosed;
    private RoomStatus status;
    private String winner;
    private String secretWord;
    private List<RevealResponse> reveal;
}
