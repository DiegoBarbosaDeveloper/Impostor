package co.edu.ustavillavo.impostor.domain.response;

import co.edu.ustavillavo.impostor.domain.enums.RoomStatus;
import lombok.Data;

@Data
public class CloseEndResponse {
    private Byte roundClosed;
    private RoomStatus status;
    
}
