package database.back.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 문항
 */
@Entity @NoArgsConstructor
@Setter @Getter
public class Question {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @JoinColumn(name = "assignment_id")
    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Assignment assignment;

    private String questionContent;

//    @JoinColumn(name = "example_id")
//    @OneToMany(fetch = LAZY, cascade = ALL)
//    private Example example;



}
