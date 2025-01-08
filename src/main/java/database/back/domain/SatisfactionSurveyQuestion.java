package database.back.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 만족도 조사 문항
 */
@Entity
@NoArgsConstructor
public class SatisfactionSurveyQuestion {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "statisfcation_survey_question_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    private String instructorName;
}
