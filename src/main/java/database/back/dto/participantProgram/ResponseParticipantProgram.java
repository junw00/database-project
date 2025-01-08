package database.back.dto.participantProgram;

import database.back.domain.Assignment;
import database.back.domain.ProgramParticipantStudent;
import database.back.domain.Student;
import database.back.dto.ResponseDto;
import database.back.dto.assignment.AssignDto;
import database.back.dto.student.ResponseStudentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseParticipantProgram {

    private Long id;
    private String programTitle;
    private int unfinishedAssignmentCount;
    private int unreadClassMaterialCount;
    private int unreadNoticeCount;
    private int unAttendanceCount;
    private int totalPoint;
    private int expectPoint;
    private List<ResponseStudentDto> team;
    private String durationType;
    private String studentName;

    private List<AssignDto> unfinished;

}
