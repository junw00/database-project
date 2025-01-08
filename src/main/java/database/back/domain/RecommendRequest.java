package database.back.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 추천 요청
 */
@Entity @NoArgsConstructor
@Getter @Setter
public class RecommendRequest {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "recommend_request_id")
    private Long id;

    private LocalDateTime requestDate;

    @JoinColumn(name = "program_id")
    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Program program;

    @JoinColumn(name = "student_num")
    @ManyToOne(fetch = LAZY)
    private Student student;


}
