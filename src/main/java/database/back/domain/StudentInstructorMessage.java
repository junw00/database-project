package database.back.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 강사 학생 메시지
 */
@Entity @NoArgsConstructor
public class StudentInstructorMessage {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "student_instructor_message_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "program_participant_student_id")
    private ProgramParticipantStudent programParticipantStudent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    private String tos;
    private String froms;

    private String content;
    private boolean isRead;
    private LocalDateTime writeDate;
}
