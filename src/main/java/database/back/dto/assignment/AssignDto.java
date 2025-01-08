package database.back.dto.assignment;

import database.back.domain.Program;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter @Setter
public class AssignDto {

    private Long id;
    private String programTitle;

    private long garbageDay;
}
