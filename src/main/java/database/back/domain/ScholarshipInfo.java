package database.back.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 장학금 내역
 */
@Entity
@NoArgsConstructor
public class ScholarshipInfo {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "scholarship_info_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_num")
    private Student student;

    private String programName;
    private int amount;
    private String description;

    private LocalDateTime paymentDate;

}
