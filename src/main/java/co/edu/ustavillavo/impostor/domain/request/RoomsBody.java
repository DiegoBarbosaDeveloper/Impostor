package co.edu.ustavillavo.impostor.domain.request;

import lombok.Data;

@Data
public class RoomsBody {
    
    private String hostNickname;
    private String category;
    private String impostorCount;

}
