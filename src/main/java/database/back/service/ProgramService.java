package database.back.service;

import database.back.domain.*;
import database.back.dto.assignment.AssignDto;
import database.back.dto.participantProgram.ResponseParticipantProgram;
import database.back.dto.student.ResponseStudentDto;
import database.back.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final MaterialRepository materialRepository;
    private final MaterialStateRepository materialStateRepository;
    private final ParticipantProgramRepository participantProgramRepository;
    private final AttendanceRepository attendanceRepository;
    private final AssignRepository assignRepository;
    private final AssignExecutionRepo assignExecutionRepo;
    private final SemesterRepository semesterRepository;
    private final RecommendRepository recommendRepository;
    private final RecommendProgramRepository recommendProgramRepository;

    public List<ResponseParticipantProgram> participantProgramsInfo(Student student) {


        List<ResponseParticipantProgram> responseParticipantPrograms = new ArrayList<>();
        List<ProgramParticipantStudent> participantProgram = participantProgramRepository.findParticipantProgram(student);
        log.info("참여 프로그램 수");
        for(ProgramParticipantStudent pps : participantProgram) {
            Program program = pps.getProgram();
            if (!program.getOperationStatus().equals("운영중")) continue;

            ResponseParticipantProgram responseParticipantProgram = new ResponseParticipantProgram();
            responseParticipantProgram.setId(program.getId());
            responseParticipantProgram.setProgramTitle(program.getProgramTitle());

            List<MaterialStateManagement> materialStateManagement = materialStateRepository.findMaterialStateManagement(pps, program);
            int notice = 0;
            int material = 0;
            for(MaterialStateManagement ms : materialStateManagement) {
                if (ms.getClassMaterial().getType().equals("공지사항") && !ms.isRead()) {
                    notice += 1;
                }else if(ms.getClassMaterial().getType().equals("수업자료") && !ms.isRead()) {
                    material += 1;
                }
            }

            if (pps.getRecommendId() != null) {
                List<ProgramParticipantStudent> team = participantProgramRepository.findTeam(pps.getRecommendId());
                if (team.size() <= 1) responseParticipantProgram.setTeam(null);
                else {
                     List<ResponseStudentDto> teamInfo = new ArrayList<>();
                     for (ProgramParticipantStudent pps1 : team) {
                         log.info("loop2312312312");
                         ResponseStudentDto responseStudentDto = new ResponseStudentDto();
                         responseStudentDto.setStudentName(pps1.getStudent().getStudentName());
                         responseStudentDto.setRole(pps1.getRole());
                         teamInfo.add(responseStudentDto);
                     }
                     responseParticipantProgram.setTeam(teamInfo);
                }
            }

            responseParticipantProgram.setDurationType(program.getDurationType());
            responseParticipantProgram.setUnreadClassMaterialCount(material);
            responseParticipantProgram.setUnreadNoticeCount(notice);

            int attendanceCount = attendanceRepository.attendanceCount(pps);
            int totalAttendance = attendanceRepository.countParticipant(pps);
            log.info("attendanceCount: {}, totalAttendance: {}", attendanceCount, totalAttendance);
            //총 남은 프로그램 일정
            int garbage = program.getTotalAssignmentNum() - totalAttendance;
//            int y = (int) (((double) 70 / 100) * 3);
            int x = (int) (((double) 70 / 100) * program.getTotalClassDay());
            log.info("x: {}", x);
            LocalDateTime currentTime = LocalDateTime.now();
            List<AssignDto> assign = new ArrayList<>();
            List<Assignment> assignments = assignRepository.findAssignments(program);
            int unfinish = 0;
            for (Assignment a : assignments) {
                AssignmentExecution assignmentExecution = assignExecutionRepo.findAssignmentExecution(a, pps);
                if (assignmentExecution == null || !assignmentExecution.isAssignmentCompletion()) {

                    // 모집기간이 현재 시간보다 미래인지 확인 남은 기간 체크 로직
                    if (a.getDeadlineDate().isAfter(currentTime)) {
                        // 현재 시간과 모집기간의 차이를 계산
                        Duration duration = Duration.between(currentTime, a.getDeadlineDate());
                        long daysLeft = duration.toDays();

                        // 3일 이내인지 확인
                        if (daysLeft <= 3) {
                            AssignDto assignDto = new AssignDto();
                            assignDto.setProgramTitle(a.getAssignmentTitle());
                            assignDto.setGarbageDay(daysLeft);
                            assignDto.setId(a.getId());
                            assign.add(assignDto);
                        }
                    }else {
                        unfinish -= 1;
                    }

                    unfinish += 1;
                }
            }
            responseParticipantProgram.setUnfinished(assign);
            responseParticipantProgram.setUnfinishedAssignmentCount(unfinish);
            responseParticipantProgram.setUnAttendanceCount(x - attendanceCount);
            responseParticipantProgram.setTotalPoint(1);
            responseParticipantProgram.setExpectPoint(1);

            responseParticipantPrograms.add(responseParticipantProgram);
        }

        return responseParticipantPrograms;
    }

    public List<Student> findStudent(List<Student> students, Long programId) {

        Program program = programRepository.findById(programId).get();
        log.info("프로그램 정보: {}", program);
        String year = String.valueOf(Year.now().getValue());
        Month month = Month.from(LocalDate.now());

        int programTenacity = program.getTenacity();
        int programGlobal = program.getGlobal();
        int programCreativity = program.getCreativity();
        int programExpert = program.getExpert();
        String semester = (month.getValue() <= 6) ? "1" : "2";
        for (Student s : students) {
            log.info("학생 정보: {}, 년도: {}, 학기: {}", s, year, semester);
            SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(s, year, semester);



            if (semesterSpecificStuInfo != null) {
                s.setRedCount(semesterSpecificStuInfo.getRecommendationCount());
                s.setBlueCount(semesterSpecificStuInfo.getApplicationCount());
                s.setYellowCount(semesterSpecificStuInfo.getRejectionCount());
                log.info("학기별 정보: {}", semesterSpecificStuInfo);
                int semesterTenacity = semesterSpecificStuInfo.getTenacity();
                int semesterGlobal = semesterSpecificStuInfo.getGlobal();
                int semesterCreativity = semesterSpecificStuInfo.getCreativity();
                int semesterExpert = semesterSpecificStuInfo.getExpert();

                if (semesterExpert >= 10 && semesterGlobal >= 10 && semesterCreativity >= 10 && semesterTenacity >= 10) {

                    continue;
                }

                if (semesterExpert < 10) {
                    if (semesterExpert + programExpert >= 10) {
                        semesterExpert +=  programExpert;
                        if (semesterExpert >= 10 && semesterGlobal >= 10 && semesterCreativity >= 10 && semesterTenacity >= 10) {
                            s.setSuccess("10점");
                            continue;
                        }
                    }
                }

                if (semesterCreativity < 10) {
                    if (semesterCreativity + programCreativity >= 10) {
                        semesterCreativity +=  programCreativity;
                        if (semesterExpert >= 10 && semesterGlobal >= 10 && semesterCreativity >= 10 && semesterTenacity >= 10) {
                            s.setSuccess("10점");
                            continue;
                        }
                    }
                }

                if (semesterGlobal < 10) {
                    if (semesterGlobal + programGlobal >= 10) {
                        semesterGlobal += programGlobal;
                        if (semesterExpert >= 10 && semesterGlobal >= 10 && semesterCreativity >= 10 && semesterTenacity >= 10) {
                            s.setSuccess("10점");
                            continue;
                        }

                    }
                }

                if (semesterTenacity < 10) {
                    if (semesterTenacity + programTenacity >= 10) {

                        semesterTenacity += programTenacity;
                        log.info("intan: {}, global: {}, expert: {}, creati: {}", semesterTenacity, semesterGlobal, semesterExpert, semesterCreativity);
                        if (semesterExpert >= 10 && semesterGlobal >= 10 && semesterCreativity >= 10 && semesterTenacity >= 10) {
                            s.setSuccess("10점");
                        }
                    }
                }
            }
        }

        return students;
    }
}
