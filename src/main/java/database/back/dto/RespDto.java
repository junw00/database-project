package database.back.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import database.back.domain.Program;
import database.back.domain.Recommend;
import database.back.domain.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter @Setter
public class RespDto {

    private Long recommendId;
    private String recommendProgramStatus;
    private LocalDateTime rejectionDate;
    private String studentName; // 학생 이름 등 필요한 필드만 포함
    private String projectName;
    private String studentNum;
    private LocalDateTime recommendDate;
    private String rejectMessage;
}
