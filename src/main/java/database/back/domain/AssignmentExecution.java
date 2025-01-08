package database.back.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 과제 수행
 */
@Entity @NoArgsConstructor
@Getter @Setter
public class AssignmentExecution {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "assignment_execution_id")
    private Long id;

    @ManyToOne(fetch =  LAZY)
    @JoinColumn(name = "program_participant_student_id")
    private ProgramParticipantStudent participantStudent;

    private boolean assignmentCompletion;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    private LocalDateTime executionDate;

    private Integer correctNum;
    private Integer incorretNum;

    @Transient
    List<Assignment> assignments;

}
