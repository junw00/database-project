package database.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 학생
 */
@Entity
@NoArgsConstructor
@Getter @Setter
@ToString
public class Student {

    @Id @Column(name = "student_num")
    private String studentNum;
    private String studentName;
    private String password;
    private String studentTel;
    private String studentMajor;

    @JoinColumn(name = "professor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Professor professor;

    //핵심 역량
    private int tenacity;
    private int global;
    private int creativity;
    private int expert;

    private int point;

    private int requestNum;
    //관심 키워드
    private boolean scholar;
    private boolean overseas;
    private boolean qualification;
    private boolean certification;

    private boolean favorite;

    @Transient
    private int redCount;
    @Transient
    private int blueCount;
    @Transient
    private int yellowCount;

    @Transient String success;
}
