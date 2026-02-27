package co.edu.ustavillavo.impostor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "impo_player")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID playerId;

    @ManyToOne
    @JoinColumn(name = "impo_play_room_id", nullable = false)
    private RoomEntity room;

    @Column(name = "impo_play_nickname", nullable = false)
    private String nickname;

    @Column(name = "impo_play_alive", nullable = false)
    private boolean alive;



}
