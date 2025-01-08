package database.back.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 과제
 */
@Entity
@NoArgsConstructor
@Setter @Getter
public class Assignment {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "assignment_id")
    private Long id;

    private LocalDateTime registrationDate;
    private LocalDateTime modificationDate;
    private LocalDateTime deadlineDate;

    @JoinColumn(name = "program_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = ALL)
    private Program program;

    private String instructorName;
    private int totalQuestion;

    private String assignmentTitle;
    @Transient
    private boolean isCompleted;

    @Transient
    private long garbageDay;

}
