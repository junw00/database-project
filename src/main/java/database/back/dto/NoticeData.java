package database.back.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class NoticeData {
    private String title;
    private String content;
    private String deadline;
}
