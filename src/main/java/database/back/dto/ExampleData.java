package database.back.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ExampleData {
    private Long id;
    private String exampleContent;
    private boolean correct;

}
