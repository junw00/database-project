package database.back.dto.instructor;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class InstructorResponseDto {

    private String instructorNum;
    private String instructorPassword;
}
