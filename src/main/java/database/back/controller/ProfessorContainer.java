package database.back.controller;

import database.back.domain.Professor;
import database.back.domain.SemesterSpecificStuInfo;
import database.back.domain.Student;
import database.back.dto.ResponseDto;
import database.back.dto.professor.RequestProfessorDto;
import database.back.repository.ProfessorRepository;
import database.back.repository.SemesterRepository;
import database.back.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProfessorContainer {

    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final SemesterRepository semesterRepository;

    @PostMapping("/professor/login")
    private ResponseEntity<?> login(@RequestBody RequestProfessorDto requestProfessorDto, HttpSession session) {
        Professor findProfessor = professorRepository.findByProfessorNum(requestProfessorDto.getProfessorNum()).orElseThrow(() -> {
            throw new NoSuchElementException("아이디 틀림");
        });

        if (!findProfessor.getProfessorPassword().equals(requestProfessorDto.getProfessorPassword())) {
            throw new NoSuchElementException("비밀번호 틀림");
        }
        session.setAttribute("professor", findProfessor);

        return ResponseEntity.ok().body(new ResponseDto<>(200, "로그인 성공", findProfessor));
    }

    @PostMapping("/professor/logout")
    private ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", true));
    }

    //학생 총 추천 거절 횟수 찾는 api
    @GetMapping("/students")
    public ResponseEntity<?> getStudents(HttpSession session) {
        Professor professor = (Professor) session.getAttribute("professor");
        Professor findProfessor = professorRepository.findByProfessorNum(professor.getProfessorNum()).orElseThrow(() -> {
            throw new NoSuchElementException("없는 회원입니다.");
        });
        List<Student> students = studentRepository.findStudentsByProfessor(findProfessor);
        String year = String.valueOf(Year.now().getValue());
        Month month = Month.from(LocalDate.now());
        String semester = (month.getValue() <= 6) ? "1" : "2";

        for(Student s : students) {
            SemesterSpecificStuInfo semesterSpecificStuInfo = semesterRepository.findSemesterSpecificStuInfo(s, year, semester);
            s.setRedCount(semesterSpecificStuInfo.getRecommendationCount());
            s.setBlueCount(semesterSpecificStuInfo.getApplicationCount());
            s.setYellowCount(semesterSpecificStuInfo.getRejectionCount());
        }
        log.info("지도학생: {}",students.toString());
        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", students));

    }

}
