package database.back.dto.assignment.requestDto;

import database.back.domain.Assignment;
import database.back.domain.Instructor;
import database.back.domain.Program;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@ToString
public class AssignmentRequestDto {

    private List<RequestQuestionDto> questions;

    private LocalDateTime deadline;
    private String assignmentTitle;


    public static Assignment uploadAssignment(AssignmentRequestDto assignmentRequestDto, Program program, Instructor instructor, String title) {
        Assignment assignment = new Assignment();
        assignment.setProgram(program);
        assignment.setDeadlineDate(assignmentRequestDto.getDeadline());
        assignment.setTotalQuestion(assignmentRequestDto.getQuestions().size());
        assignment.setInstructorName(instructor.getInstructorName());
        assignment.setRegistrationDate(LocalDateTime.now());
        assignment.setAssignmentTitle(title);

        return assignment;
    }

}
