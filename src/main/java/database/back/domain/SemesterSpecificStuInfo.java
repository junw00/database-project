package database.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 학기별 학생 정보
 */
@Entity @NoArgsConstructor
@Getter @Setter
@ToString
public class SemesterSpecificStuInfo {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "semetser_specific_stu_id")
    private Long id;

    @JoinColumn(name = "student_num")
    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    private Student student;

    private String year;
    private String semester;

    //핵심 역량
    private int tenacity;
    private int global;
    private int creativity;
    private int expert;

    private int recommendationCount;
    private int rejectionCount;
    private int applicationCount;
    private int recommendationRequestCount;
}
