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
 * 공지사항 및 수업자료
 */
@Entity
@NoArgsConstructor
@Getter @Setter
@ToString
public class ClassMaterial {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "class_mateiral_id")
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "program_id")
    @JsonIgnore
    private Program program;

    private LocalDateTime uploadDate;
    private LocalDateTime modificationDate;
    private LocalDateTime deleteDate;

    private String instructorName;

    private String filePath;

    private String type;




    @Transient
    private String read;
}
