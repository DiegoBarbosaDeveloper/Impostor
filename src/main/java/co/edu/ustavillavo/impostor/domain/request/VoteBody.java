package co.edu.ustavillavo.impostor.domain.request;

import java.util.UUID;

import lombok.Data;

@Data
public class VoteBody {
    private UUID votedId;
}
