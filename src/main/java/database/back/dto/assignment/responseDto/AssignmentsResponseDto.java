package database.back.dto.assignment.responseDto;

import database.back.domain.Question;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@ToString
public class AssignmentsResponseDto {

    private List<Question> questions;

    private LocalDateTime submittedDate;
}
