package database.back.dto.assignment.requestDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
public class RequestQuestionDto {

    private String question;
    private List<String> options;
    private int answer;
}
