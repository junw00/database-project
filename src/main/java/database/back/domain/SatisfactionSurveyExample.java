package database.back.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 만족도 조사 선지
 */

@Entity
@NoArgsConstructor
public class SatisfactionSurveyExample {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "statisfaction_survey_example_id")
    private Long id;

    @JoinColumn(name = "statisfaction_survey_questions_id")
    @ManyToOne(fetch = LAZY)
    private SatisfactionSurveyQuestion satisfactionSurveyQuestion;


}
