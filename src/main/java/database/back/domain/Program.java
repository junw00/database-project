package database.back.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import database.back.dto.program.ResponseProgramDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 비교과 프로그램
 */
@Entity
@NoArgsConstructor
@Getter @Setter
@ToString
public class Program {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "program_id")
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

    @Transient
    private String recommendState;

    private int totalAssignmentNum;
    private LocalDateTime recruitmentPeriod;
    private int totalClassDay;
    private String year;
    private String semester;
    /**
     * 모집종료, 운영중, 모집중, 운영종료
     */
    private String operationStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "instructor_id")
    @JsonIgnore
    private Instructor instructor;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;

    //
    @Transient
    private long garbageTime;

    //남은인원 계산
    @Transient
    private int garbage;

    @Transient
    private boolean dunning;

    public ResponseProgramDto toDto(Program program, String instructorName) {
        ResponseProgramDto responseProgramDto = new ResponseProgramDto();
        responseProgramDto.setId(program.getId());
        responseProgramDto.setExpert(program.getExpert());
        responseProgramDto.setProgramTitle(program.getProgramTitle());
        responseProgramDto.setCreativity(program.getCreativity());
        responseProgramDto.setProgramContent(program.getProgramContent());
        responseProgramDto.setGlobal(program.getGlobal());
        responseProgramDto.setDurationType(program.getDurationType());
        responseProgramDto.setTenacity(program.getTenacity());
        responseProgramDto.setPoint(program.getPoint());
        responseProgramDto.setEndDate(program.getEndDate());
        responseProgramDto.setStartDate(program.getStartDate());
        responseProgramDto.setImagePath(program.getImagePath());
        responseProgramDto.setParticipantNum(program.getParticipantNum());
        responseProgramDto.setRecruitmentPeriod(program.getRecruitmentPeriod());
        responseProgramDto.setSemester(program.getSemester());
        responseProgramDto.setYear(program.getYear());
        responseProgramDto.setRegistrationDate(program.getRegistrationDate());
        responseProgramDto.setTargetStudent(program.getTargetStudent());
        responseProgramDto.setTotalClassDay(program.getTotalClassDay());
        responseProgramDto.setOperationStatus(program.getOperationStatus());
        responseProgramDto.setInstructorName(instructorName);

        return responseProgramDto;
    }

//    public ProgramResponseDto toDto(Program program) {
//        ProgramResponseDto programResponseDto = new ProgramResponseDto();
////        programResponseDto.
//    }


}
