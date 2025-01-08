package database.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ResponseDto<T> {

    private int codeNum;
    private String message;
    private T data;
}
