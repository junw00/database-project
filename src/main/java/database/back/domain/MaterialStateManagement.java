package database.back.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 자료 상태 관리
 */
@Entity @NoArgsConstructor
@Getter @Setter
@ToString
public class MaterialStateManagement {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "material_state_id")
    private Long id;

    @JoinColumn(name = "program_participant_student_id")
    @ManyToOne(fetch = LAZY)
    private ProgramParticipantStudent participantStudent;

    @JoinColumn(name = "class_mateiral_id")
    @ManyToOne(fetch = LAZY, cascade = ALL)
    private ClassMaterial classMaterial;

    @JoinColumn(name = "program_id")
    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Program program;

    private boolean isRead;


}
