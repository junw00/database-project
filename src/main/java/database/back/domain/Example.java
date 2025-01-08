package database.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 선지
 */
@Entity
@NoArgsConstructor
@Getter @Setter
public class Example {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "example_id")
    private Long id;

    private String exampleContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;

    private boolean isCorrect;
}
