package database.back.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 교수
 */
@Entity
@NoArgsConstructor
@Getter @Setter
@ToString
public class Professor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "professor_id")
    private Long id;

    private String professorNum;
    private String professorPassword;
    private String professorName;
    private String department;
    private String professorTel;
    private String professorOffice;
}
