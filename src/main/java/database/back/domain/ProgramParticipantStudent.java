package database.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 프로그램 참여 학생
 */
@Entity @NoArgsConstructor
@Getter @Setter
@ToString
public class ProgramParticipantStudent {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "program_participant_student_id")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "student_num")
//    @JsonIgnore
    private Student student;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "program_id")
    @JsonIgnore
    private Program program;

    private String attendanceRate;

    private int unfinishedAssignmentNum;

    private boolean satisfactionSurvey;

    private boolean isCompleted;

    private Long recommendId;
    private String role;
}
