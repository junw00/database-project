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
 * 관리자
 */
@Entity @NoArgsConstructor
@Getter @Setter
public class Admin {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(unique = true)
    private String adminNum;
    private String password;
    private String adminName;

}
