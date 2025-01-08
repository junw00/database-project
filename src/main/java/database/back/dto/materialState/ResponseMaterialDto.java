package database.back.dto.materialState;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ResponseMaterialDto {

    private Long id;

    private String programTitle;
    private String durationType;

    private String programContent;

    private LocalDateTime registrationDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int participantNum;

    private String imagePath;

    /**
     * 전체, 또는 학과 도메인
     */
    private String targetStudent;
    private int point;

    //핵심 역량
    private int tenacity;
    private int global;
    private int creativity;
    private int expert;

    private int totalAssignmentNum;
    private LocalDateTime recruitmentPeriod;
    private int totalClassDay;
    private String year;
    private String semester;
    /**
     * 모집종료, 운영중, 모집중, 운영종료
     */
    private String operationStatus;
}
