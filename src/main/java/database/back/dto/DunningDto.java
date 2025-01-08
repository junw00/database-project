package database.back.dto;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class DunningDto {

    private Long recommendId;
    private String studentNum;
}
