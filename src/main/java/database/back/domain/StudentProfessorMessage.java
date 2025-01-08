package database.back.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 학생 교수 메시지
 */

@Entity @NoArgsConstructor
public class StudentProfessorMessage {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "student_professor_message_id")
    private Long id;

    @JoinColumn(name = "professor_id")
    @ManyToOne(fetch = LAZY)
    private Professor professor;

    private String content;
    private LocalDateTime writeDate;
    private String tos;
    private String froms;
    private boolean isRead;

    @JoinColumn(name = "student_num")
    @ManyToOne(fetch = LAZY)
    private Student student;


}
