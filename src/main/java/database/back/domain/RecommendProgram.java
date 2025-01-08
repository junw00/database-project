package database.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 추천 프로그램
 */
@Entity @NoArgsConstructor
@Getter @Setter
@ToString
public class RecommendProgram {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "recommend_program_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_num")
    @JsonIgnore
    private Student student;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recommend_id")
    @JsonIgnore
    private Recommend recommend;

    /**
     * 팀장, 팀원, 개별
     */
    private String role;

    private String recommendProgramStatus;

    private String rejectionReason;
    private LocalDateTime rejectionDate;
    private String year;
    private String semester;

    //독촉
    private boolean dunning;
    private int dunningNum;

    @Transient
    private Program program;
}
