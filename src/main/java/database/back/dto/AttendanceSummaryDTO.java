package database.back.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AttendanceSummaryDTO {
    private LocalDateTime classDate;
    private Long presentStudents;
    private Long absentStudents;
}
