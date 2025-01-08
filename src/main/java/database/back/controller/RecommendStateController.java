package database.back.controller;

import database.back.domain.Professor;
import database.back.domain.Program;
import database.back.domain.RecommendProgram;
import database.back.domain.Student;
import database.back.dto.ResponseDto;
import database.back.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendStateController {

    private final RecommendRepository recommendRepository;
    private final RecommendProgramRepository recommendProgramRepository;
    private final ProgramRepository programRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    @PostMapping("/recommend/read/{id}")
    @Transactional
    public ResponseEntity<?> read(@PathVariable Long id, HttpSession session) {
        Program program = programRepository.findById(id).get();
        log.info("program: {}", program);
        Student student = (Student) session.getAttribute("userInfo");
        Student findStudent = studentRepository.findById(student.getStudentNum()).get();
        log.info("student:{}", student);
        Professor professor = findStudent.getProfessor();
        log.info("professor: {}", professor.toString());
        String year = String.valueOf(Year.now().getValue());
        Month month = Month.from(LocalDate.now());
        String semester = (month.getValue() <= 6) ? "1" : "2";
        log.info("year: {}, semester:{}", year, semester);

//        professorRepository
        RecommendProgram recommendProgram = recommendProgramRepository.findRecommendProgram(program, professor, findStudent, semester, year);
        if (!(recommendProgram.getRecommendProgramStatus().equals("신청 완료") || recommendProgram.getRecommendProgramStatus().equals("거절"))){
            recommendProgram.setRecommendProgramStatus("읽음");
        }

        log.info("추천 프로그램: {}", recommendProgram);

        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", recommendProgram));
    }
}
