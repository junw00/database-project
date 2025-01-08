package database.back.dto.student;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class RequestStudentDto {

    private String studentId;
    private String studentPassword;
    private String studentName;
    private String role;
}
