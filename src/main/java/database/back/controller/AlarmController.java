package database.back.controller;

import database.back.domain.Program;
import database.back.domain.ProgramParticipantStudent;
import database.back.domain.Student;
import database.back.repository.ParticipantProgramRepository;
import database.back.repository.RecommendProgramRepository;
import database.back.repository.RecommendRepository;
import database.back.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final RecommendProgramRepository recommendProgramRepository;
    private final RecommendRepository recommendRepository;
    private final StudentRepository studentRepository;

    //추천한 프로그램의 운영 시작일이 3일밖에 안남은 경우 그리고 모집 인원이 남은 수
    @GetMapping("/alarm/recommend/program")
    public ResponseEntity<?> getAlarm(HttpSession session) {
        Student student = (Student) session.getAttribute("userInfo");
        Student findStudent = studentRepository.findById(student.getStudentNum()).get();

        LocalDateTime currentTime = LocalDateTime.now();

        List<Program> recommendProgram = recommendRepository.findRecommendProgram(findStudent);

        for (Program p : recommendProgram) {
            LocalDateTime recruitmentPeriod = p.getRecruitmentPeriod();

            // 모집기간이 현재 시간보다 미래인지 확인
            if (recruitmentPeriod.isAfter(currentTime)) {
                // 현재 시간과 모집기간의 차이를 계산
                Duration duration = Duration.between(currentTime, recruitmentPeriod);
                long daysLeft = duration.toDays();


            }

        }

        return null;
    }



}
