package database.back.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 강사
 */
@Entity @NoArgsConstructor
@Getter @Setter
public class Instructor {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "instructor_id")
    private Long id;

    @Column(unique = true)
    private String instructorNum;

    private String instructorPassword;

    private String instructorName;
    private String instructorTel;


}
