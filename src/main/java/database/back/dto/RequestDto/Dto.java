package database.back.dto.RequestDto;

import database.back.domain.Program;
import lombok.Data;

import java.util.List;

@Data
public class Dto {
    private List<String> students;
    private Long id;

    private String teamLeader;
}
