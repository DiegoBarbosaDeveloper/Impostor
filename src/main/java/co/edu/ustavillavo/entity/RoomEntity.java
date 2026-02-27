package co.edu.ustavillavo.entity;

import co.edu.ustavillavo.impostor.domain.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "impo_room")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID RoomId;

    @Column(name = "impo_room_code", nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "impo_room_status", nullable = false)
    private RoomStatus status;

    @OneToOne
    @JoinColumn(name = "impo_room_host_player_id", nullable = false)
    private PlayerEntity hostPlayer;

    @Column(name = "impo_room_category", nullable = false)
    private String category;

    @Column(name = "impo_room_impostor_count", nullable = false)
    private int impostorCount;

    @Column(name = "impo_room_current_round")
    private Byte currentRound;

    @Column(name = "impo_room_secret_word", nullable = false)
    private String secretWord;

    @Column(name = "impo_room_winner_team")
    private String  winnerTeam;

}
