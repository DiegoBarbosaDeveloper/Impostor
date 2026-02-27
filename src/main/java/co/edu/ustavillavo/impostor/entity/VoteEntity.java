package co.edu.ustavillavo.impostor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "impo_vote")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID voteId;

    @ManyToOne
    @JoinColumn(name = "impo_vote_room_id")
    private RoomEntity room;

    @Column(name = "impo_vote_round_number", nullable = false)
    private int roundNumber;

    @OneToOne
    @JoinColumn(name = "impo_vote_voter_id", nullable = false)
    private PlayerEntity voterPlayer;

    @OneToOne
    @JoinColumn(name = "impo_vote_voted_id")
    private PlayerEntity votedPlayer;
}
