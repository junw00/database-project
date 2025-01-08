package database.back.controller;

import database.back.domain.Student;
import database.back.dto.ResponseDto;
import database.back.dto.student.RequestStudentDto;
import database.back.repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;
    
    @PostMapping("/student/login")
    public ResponseEntity<?> studentLogin(@RequestBody RequestStudentDto requestStudentDto, HttpSession session) {

        Student student = studentRepository.findById(requestStudentDto.getStudentId()).orElseThrow(() -> {
            throw new NoSuchElementException("존재하지 않는 회원");
        });
        log.info("student: {}", student.toString());
        if(!student.getPassword().equals(requestStudentDto.getStudentPassword())) {
            return ResponseEntity.ok().body(new ResponseDto<>(200, "비밀번호 틀림", null));
        }

        session.setAttribute("userInfo", student);
        Student userInfo = (Student) session.getAttribute("userInfo");
        log.info("loginStudent: {}", userInfo);
        return ResponseEntity.ok().body(new ResponseDto<>(200, "로그인 성공", student));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", true));
    }
}
