package database.back.controller;

import database.back.domain.*;
import database.back.dto.DunningDto;
import database.back.dto.Message;
import database.back.dto.RequestDto.Dto;
import database.back.dto.RespDto;
import database.back.dto.ResponseDto;
import database.back.dto.participantProgram.ResponseParticipantProgram;
import database.back.dto.program.ResponseProgramDto;
import database.back.dto.student.RequestStudentDto;
import database.back.repository.*;
import database.back.service.ProgramService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramRepository programRepository;
    private final StudentRepository studentRepository;
    private final RecommendRepository recommendRepository;
    private final ProgramService programService;
    private final ProfessorRepository professorRepository;
    private final RecommendProgramRepository recommendProgramRepository;
    private final SemesterRepository semesterRepository;
    private final ParticipantProgramRepository participantProgramRepository;

    // 모든 프로그램 조회
    @GetMapping("/programs")
    public ResponseEntity<?> getPrograms() {
        List<Program> allPrograms;
        List<ResponseProgramDto> responseProgramDtos;
        try {
            allPrograms = programRepository.findAll();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", allPrograms.stream().map(p -> p.toDto(p, p.getInstructor().getInstructorName())).toList()));
    }

    // 학생이 참여중인 프로그램
    @PostMapping("/participant")
    public ResponseEntity<?> getParticipantProgram(HttpSession session) {

        Student student = (Student) session.getAttribute("userInfo");
        Student findStudent = studentRepository.findById(student.getStudentNum()).orElseThrow(() -> {
            throw new NoSuchElementException("로그인 안 되어있음.");
        });

        List<ResponseParticipantProgram> responseParticipantPrograms = programService.participantProgramsInfo(findStudent);

        log.info("신청한 프로그램 정보: {}", responseParticipantPrograms.toString());

        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", responseParticipantPrograms));
    }

    // 추천 프로그램 찾기
    @GetMapping("/recommend")
    @Transactional
    public ResponseEntity<?> getProgram(HttpSession session) {
        Student student = (Student) session.getAttribute("userInfo");
        String year = String.valueOf(Year.now().getValue());
        Month month = Month.from(LocalDate.now());
        String semester = (month.getValue() <= 6) ? "1" : "2";
//        log.info("findStudent: {}", student.getStudentName());
        if(student != null) {
            Student findStudent = studentRepository.findById(student.getStudentNum()).get();
            Professor professor = findStudent.getProfessor();
            List<Program> recommendProgram = recommendRepository.findRecommendProgram(findStudent);

            //현재시간 가져오기
            LocalDateTime currentTime = LocalDateTime.now();

            for(Program p : recommendProgram) {

                //모집기간 가져오기
                LocalDateTime recruitmentPeriod = p.getRecruitmentPeriod();

                RecommendProgram findRecommends = recommendProgramRepository.findRecommendProgram(p, professor, findStudent, semester, year);
                log.info("findRecommends: {}", findRecommends);
                if(findRecommends == null) continue;

                p.setRecommendState(findRecommends.getRecommendProgramStatus());
                p.setDunning(findRecommends.isDunning());
                //남은인원
                List<ProgramParticipantStudent> participantAll = participantProgramRepository.findParticipantProgram111(p);
                if(participantAll != null) {
                    int garbageP = p.getParticipantNum() - participantAll.size();
                    if (garbageP <= 10 && !p.getRecommendState().equals("신청 완료")) {
                        p.setGarbage(garbageP);
                    }
                }



                // 모집기간이 현재 시간보다 미래인지 확인 남은 기간 체크 로직
                if (recruitmentPeriod.isAfter(currentTime)) {
                    // 현재 시간과 모집기간의 차이를 계산
                    Duration duration = Duration.between(currentTime, recruitmentPeriod);
                    long daysLeft = duration.toDays();

                    // 3일 이내인지 확인
                    if (daysLeft <= 3 && (p.getRecommendState().equals("읽음") || p.getRecommendState().equals("안읽음"))) {
                        p.setGarbageTime(daysLeft);
                    }
                }

            }

            log.info("findStudent: {}", findStudent.toString());
            return ResponseEntity.ok().body(new ResponseDto<>(200, "success", recommendProgram));
        }
        return ResponseEntity.ok().body(new ResponseDto<>(202, "로그인 안됨 추천 프로그램", null));
    }

    //프로그램과 관련된 학생 찾기
    @GetMapping("/recommend/{programId}")
    public ResponseEntity<?> s(@PathVariable Long programId, HttpSession session) {
        log.info("값넘어옴?: {}", programId);
        Professor professor = (Professor) session.getAttribute("professor");
        Professor findProfessor = professorRepository.findByProfessorNum(professor.getProfessorNum()).get();
        //지도학생
        List<Student> students = studentRepository.findStudentsByProfessor(findProfessor);

        List<Student> student = programService.findStudent(students, programId);

        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", student));
    }

    //학생이 신청
    @PostMapping("/recommend/{id}")
    @Transactional
    public ResponseEntity<?> recommend(HttpSession session, @PathVariable Long id) {
        Student student = (Student) session.getAttribute("userInfo");
        log.info("ㅊ천: {}", id);
        Student findStudent = studentRepository.findById(student.getStudentNum()).get();
        Professor professor = student.getProfessor();
        Program program = programRepository.findById(id).get();

        String year = String.valueOf(Year.now().getValue());
        Month month = Month.from(LocalDate.now());
        String semester = (month.getValue() <= 6) ? "1" : "2";

        RecommendProgram recommendProgram = recommendProgramRepository.findRecommendProgram(program, professor, findStudent, semester, year);
        log.info("recommendProgram: {}", recommendProgram.toString());
        ProgramParticipantStudent participantProgram = participantProgramRepository.findParticipantProgram(findStudent.getStudentNum(), program.getId());
//        log.info("participant:{}", participantProgram.toString());
        if(participantProgram != null) {

            return ResponseEntity.ok().body(new ResponseDto<>(200, "이미 신청함", "이미 신청한 프로그램 입니다."));
        }else {
            SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(findStudent, year, semester);
            ProgramParticipantStudent programParticipantStudent = new ProgramParticipantStudent();
            programParticipantStudent.setProgram(recommendProgram.getRecommend().getProgram());
            programParticipantStudent.setRole(recommendProgram.getRole());
            programParticipantStudent.setRecommendId(recommendProgram.getRecommend().getId());
            programParticipantStudent.setCompleted(false);
            programParticipantStudent.setStudent(findStudent);
            programParticipantStudent.setSatisfactionSurvey(false);
            programParticipantStudent.setAttendanceRate("0");
            programParticipantStudent.setUnfinishedAssignmentNum(0);
            recommendProgram.setRecommendProgramStatus("신청 완료");
            semesterSpecificStuInfo.setApplicationCount(semesterSpecificStuInfo.getApplicationCount() + 1);
            participantProgramRepository.save(programParticipantStudent);
            return ResponseEntity.ok().body(new ResponseDto<>(200, "신청 완료", "신청 완료"));
        }

    }
    @GetMapping("/recommend/state")
    public ResponseEntity<?> getRecommendState(HttpSession session) {
        Professor professor = (Professor) session.getAttribute("professor");
        Professor findProfessor = professorRepository.findById(professor.getId()).get();
        List<RecommendProgram> recommendState = recommendProgramRepository.findRecommendState(findProfessor);
        List<RespDto> respDtos = new ArrayList<>();

        for(RecommendProgram r : recommendState) {
            RespDto respDto = new RespDto();
            respDto.setRecommendProgramStatus(r.getRecommendProgramStatus());
            respDto.setRecommendId(r.getRecommend().getId());
            respDto.setRecommendDate(r.getRecommend().getRecommendDate());
            respDto.setRejectionDate(r.getRejectionDate());
            respDto.setStudentName(r.getStudent().getStudentName());
            respDto.setStudentNum(r.getStudent().getStudentNum());
            respDto.setProjectName(r.getRecommend().getProgram().getProgramTitle());
            if (r.getRecommendProgramStatus() != null && r.getRecommendProgramStatus().equals("거절")) {
                respDto.setRejectMessage(r.getRejectionReason());
            }
            respDtos.add(respDto);
        }

        return ResponseEntity.ok().body(new ResponseDto<>(200, "성공", respDtos));

    }

//    추천 거절
    @PostMapping("/recommend/reject/{programId}")
    @Transactional
    public ResponseEntity<?> rejectRecommend(@PathVariable Long programId, HttpSession session, @RequestBody Message message) {
        log.info("sdadasdadas:");
        Student student = (Student) session.getAttribute("userInfo");
        Student findStudent = studentRepository.findById(student.getStudentNum()).get();
        Professor professor = findStudent.getProfessor();
        Program program = programRepository.findById(programId).get();

        String year = String.valueOf(Year.now().getValue());
        Month month = Month.from(LocalDate.now());
        String semester = (month.getValue() <= 6) ? "1" : "2";


        RecommendProgram recommendProgram = recommendProgramRepository.findRecommendProgram(program, professor, student, semester, year);
        if (recommendProgram.getRecommendProgramStatus().equals("신청 완료")) {
            return ResponseEntity.ok().body(new ResponseDto<>(200, "이미 신청함", "이미 신청함"));
        }else {
            SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(findStudent, year, semester);
            recommendProgram.setRecommendProgramStatus("거절");
            recommendProgram.setRejectionReason(message.getRejectMessage());
            recommendProgram.setRejectionDate(LocalDateTime.now());
            semesterSpecificStuInfo.setRejectionCount(semesterSpecificStuInfo.getRejectionCount() + 1);
            return ResponseEntity.ok().body(new ResponseDto<>(200, "거절", "거절"));
        }
    }

    //그룹 추천 또는 개별 추천
    @PostMapping("/recommend")
    @Transactional
    public ResponseEntity<?> recommendProgram(@RequestBody Dto dto, HttpSession session) {
        List<String> studentsNum = dto.getStudents();
        log.info("studentNum 111: {}", dto.toString());
        Program program = programRepository.findById(dto.getId()).get();
        Professor professor = (Professor) session.getAttribute("professor");
        Professor findProfessor = professorRepository.findById(professor.getId()).get();
        Recommend recommend = new Recommend();
        recommend.setProgram(program);
        recommend.setRecommendDate(LocalDateTime.now());
        recommend.setProfessor(findProfessor);

        List<Recommend> findRecommends = recommendRepository.getRecommend(program, professor);

        String year = String.valueOf(Year.now().getValue());
        Month month = Month.from(LocalDate.now());

        String semester = (month.getValue() <= 6) ? "1" : "2";
        log.info("dto.getTeamLeader: {}", dto.getTeamLeader());
        //개별 추천
        if (dto.getTeamLeader() == null) {
            log.info("null안 :");
            if (findRecommends.isEmpty()) {

                for(String s : studentsNum) {
                    log.info("321312321321");
                    Student findStudent = studentRepository.findById(s).get();

                    SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(findStudent, year, semester);
                    RecommendProgram findRecommendProgram = recommendProgramRepository.findRecommendProgram(year, semester, findStudent, program);
                    log.info("findRecommendProgram : {}", findRecommendProgram);
                    if(findRecommendProgram != null) {
                        return ResponseEntity.ok().body(new ResponseDto<>(500, "success", "이미 추천한 프로그램입니다."));
                    }
                    if(semesterSpecificStuInfo.getRecommendationCount() > 4 ) {
                        return ResponseEntity.ok().body(new ResponseDto<>(500, "success", false));
                    }
                    Recommend save = recommendRepository.save(recommend);
                    RecommendProgram recommendProgram = new RecommendProgram();
                    recommendProgram.setRole("개별");
                    recommendProgram.setRecommendProgramStatus("안읽음");
                    recommendProgram.setYear(year);
                    recommendProgram.setSemester(semester);
                    recommendProgram.setStudent(findStudent);
                    recommendProgram.setRecommend(save);
                    recommendProgramRepository.save(recommendProgram);

                    semesterSpecificStuInfo.setRecommendationCount(semesterSpecificStuInfo.getRecommendationCount() + 1);
                }
                return ResponseEntity.ok().body(new ResponseDto<>(200, "success", true));
            }

            for (Recommend r : findRecommends) {
                log.info("reco");
//                log.info("dotStudents: {}", students);
                for (String s : studentsNum) {
                    log.info("ssss: {}", s);
                    Student student = studentRepository.findById(s).orElseThrow(() -> {
                        throw new NoSuchElementException("회원을 찾을 수 없습니다.");
                    });

                    RecommendProgram recommendProgram = recommendProgramRepository.findRecommendProgram(year, semester, student, program);
                    if (recommendProgram != null) {
                        return ResponseEntity.ok().body(new ResponseDto<>(200, "fail", "이미 추천한 프로그램입니다."));
                    }

                }

                for(String s : studentsNum) {
                    Student findStudent = studentRepository.findById(s).get();
                    SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(findStudent, year, semester);
                    if(semesterSpecificStuInfo.getRecommendationCount() > 4) {
                        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", "추천5회"));
                    }

                }

                for(String s : studentsNum) {
                    Student findStudent = studentRepository.findById(s).get();
                    Recommend save = recommendRepository.save(recommend);
                    RecommendProgram recommendProgram = new RecommendProgram();
                    recommendProgram.setRole("개별");
                    recommendProgram.setRecommendProgramStatus("안읽음");
                    recommendProgram.setYear(year);
                    recommendProgram.setSemester(semester);
                    recommendProgram.setStudent(findStudent);
                    recommendProgram.setRecommend(save);
                    recommendProgramRepository.save(recommendProgram);
                    SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(findStudent, year, semester);
                    semesterSpecificStuInfo.setRecommendationCount(semesterSpecificStuInfo.getRecommendationCount() + 1);
                }
                return ResponseEntity.ok().body(new ResponseDto<>(200, "success", "성공"));
            }


        } else {
            // 팀 추천
            if (findRecommends.isEmpty()) {
                Recommend save = recommendRepository.save(recommend);
                for (String s : studentsNum) {
                    Student findStudent = studentRepository.findById(s).get();
                    SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(findStudent, year, semester);
                    log.info("getRecommendationCount: {}", semesterSpecificStuInfo.getRecommendationCount());
                    if (semesterSpecificStuInfo.getRecommendationCount() > 4) {
                        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", "추천5회"));
                    }
                }
                for (String s : studentsNum) {
                    RecommendProgram recommendProgram = new RecommendProgram();
                    Student findStudent = studentRepository.findById(s).get();
                    if (dto.getTeamLeader().equals(s)) {
                        recommendProgram.setRole("팀장");
                    } else {
                        recommendProgram.setRole("팀원");
                    }
                    recommendProgram.setRecommendProgramStatus("안읽음");
                    recommendProgram.setYear(year);
                    recommendProgram.setSemester(semester);
                    recommendProgram.setStudent(findStudent);
                    recommendProgram.setRecommend(save);
                    recommendProgramRepository.save(recommendProgram);
                }
                return ResponseEntity.ok().body(new ResponseDto<>(200, "success", "성공"));
            } else {

                for (String s : studentsNum) {
                    Student findStudent = studentRepository.findById(s).get();

                    SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(findStudent, year, semester);
                    log.info("getRecommendationCount: {}", semesterSpecificStuInfo.getRecommendationCount());
                    if (semesterSpecificStuInfo.getRecommendationCount() > 4) {
                        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", "추천5회"));
                    }
                }
                for (String s : studentsNum) {
                    Recommend save = recommendRepository.save(recommend);
                    RecommendProgram recommendProgram = new RecommendProgram();
                    Student findStudent = studentRepository.findById(s).get();
                    if (dto.getTeamLeader().equals(s)) {
                        recommendProgram.setRole("팀장");
                    } else {
                        recommendProgram.setRole("팀원");
                    }
                    recommendProgram.setRecommendProgramStatus("안읽음");
                    recommendProgram.setYear(year);
                    recommendProgram.setSemester(semester);
                    recommendProgram.setStudent(findStudent);
                    recommendProgram.setRecommend(save);
                    recommendProgramRepository.save(recommendProgram);
                    SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(findStudent, year, semester);
                    semesterSpecificStuInfo.setRecommendationCount(semesterSpecificStuInfo.getRecommendationCount() + 1);
                }
                return ResponseEntity.ok().body(new ResponseDto<>(200, "success", "성공"));
            }
        }
        return null;
    }

    @PostMapping("/dunning")
    @Transactional
    public ResponseEntity<?> dunning(HttpSession session, @RequestBody DunningDto dto) {
        Professor professor = (Professor) session.getAttribute("professor");
        Professor findProfessor = professorRepository.findById(professor.getId()).get();
        RecommendProgram recommendProgramByRecommendIdAndStudent = recommendProgramRepository.findRecommendProgramByRecommendIdAndStudent(dto.getRecommendId(), dto.getStudentNum(), findProfessor);
        recommendProgramByRecommendIdAndStudent.setDunning(true);
        recommendProgramByRecommendIdAndStudent.setDunningNum(recommendProgramByRecommendIdAndStudent.getDunningNum() + 1);

        return ResponseEntity.ok().body(new ResponseDto<>(200, "성공", recommendProgramByRecommendIdAndStudent));
    }

}
