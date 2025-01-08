package database.back.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 과제 제출 문항
 */
@Entity @NoArgsConstructor
public class SubmittedAssignmentSentence {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "submitted_assignment_sentence_id")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "assignment_execution_id")
    private AssignmentExecution assignmentExecution;

    private String submitExample;
}
