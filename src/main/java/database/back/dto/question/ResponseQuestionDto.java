package database.back.dto.question;

import database.back.domain.Example;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
public class ResponseQuestionDto {

    private Long questionId;
    private String questionContent;
    private List<Example> examples;
}
