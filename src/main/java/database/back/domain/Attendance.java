package database.back.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 출석
 */
@Entity
@NoArgsConstructor
public class Attendance {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    private LocalDateTime classDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    private boolean attendanceStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "program_participant_student_id")
    private ProgramParticipantStudent participantStudent;

    private LocalDateTime modificationDate;

}
