package co.edu.ustavillavo.entity;

import co.edu.ustavillavo.impostor.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "impo_assigment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "impo_assi_id")
    private UUID assignmentId;

    @ManyToOne
    @JoinColumn(name = "impo_assi_room_id", nullable = false)
    private RoomEntity room;

    @OneToOne
    @JoinColumn(name = "impo_assi_player")
    private PlayerEntity player;

    @Enumerated(EnumType.STRING)
    @Column(name = "impo_assi_role", nullable = false)
    private Role role;

    @Column(name = "impo_assi_word", nullable = false)
    private String word;


}
