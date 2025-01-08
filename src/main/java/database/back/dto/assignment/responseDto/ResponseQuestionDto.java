package database.back.dto.assignment.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ResponseQuestionDto {

    private Long id; // Question ID
    private String questionContent; // 문제 내용
    private List<ExampleDto> examples; // 예제 목록

    public ResponseQuestionDto(Long id, String questionContent, List<ExampleDto> examples) {
        this.id = id;
        this.questionContent = questionContent;
        this.examples = examples;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ExampleDto {
        private String exampleContent; // 예제 내용
        private boolean isCorrect; // 정답 여부
    }
}
