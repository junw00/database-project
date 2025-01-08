package database.back.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 학생 학습포인트 내역
 */
@Entity @NoArgsConstructor
public class StudentPointInfo {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "student_point_info_id")
    private Long id;

    private int point;

    private int balance;
    private String title;
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_num")
    private Student student;



}
