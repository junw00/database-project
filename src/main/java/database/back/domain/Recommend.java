package database.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 추천
 */
@Entity @NoArgsConstructor
@Setter @Getter
public class Recommend {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "recommend_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "program_id")
    @JsonIgnore
    private Program program;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "professor_id")
    @JsonIgnore
    private Professor professor;

    private LocalDateTime recommendDate;

    //mappedBy
}
