package database.back.controller;

import database.back.domain.*;
import database.back.dto.NoticeData;
import database.back.dto.ResponseDto;
import database.back.dto.instructor.InstructorResponseDto;
import database.back.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InstructorController {

    private final ProgramRepository programRepository;
    private final MaterialRepository materialRepository;
    private final InstructorRepository instructorRepository;
    private final ParticipantProgramRepository participantProgramRepository;
    private final MaterialStateRepository materialStateRepository;

    @PostMapping("/instructor/login")
    private ResponseEntity<?> instructorLogin(HttpSession session, @RequestBody InstructorResponseDto dto) {

        log.info("dtoooo: {}", dto.toString());

        Instructor instructor = instructorRepository.findInstructor(dto.getInstructorNum()).orElseThrow(() -> {
            throw new NoSuchElementException("회원이 존재하지 않습니다.");
        });

        if(!instructor.getInstructorPassword().equals(dto.getInstructorPassword())) {
            return ResponseEntity.ok().body(new ResponseDto<>(200, "비밀번호 틀림", null));
        }

        session.setAttribute("instructor", instructor);
        return ResponseEntity.ok().body(new ResponseDto<>(200, "로그인 성공", instructor));
    }


    //강사 프로그램 가져오기
    @GetMapping("/instructor/program")
    public ResponseEntity<?> a(HttpSession session) {
        Instructor instructor = (Instructor) session.getAttribute("instructor");
        Instructor findInstructor = instructorRepository.findById(instructor.getId()).orElseThrow(() -> {
            throw new NoSuchElementException("존재하지 않는 회원입니다.");
        });
        List<Program> program = programRepository.findProgram(findInstructor);
        log.info("12313123123123213");
        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", program));
    }

    //공지등록
    @PostMapping("/program/{id}/notice")
    public ResponseEntity<?> b(@PathVariable Long id, @RequestBody NoticeData noticeData, HttpSession session) {
        log.info("공지공지: {}", id);

        Instructor instructor = (Instructor) session.getAttribute("instructor");
        Instructor findInstructor = instructorRepository.findInstructor(instructor.getInstructorNum()).get();
        Program program = programRepository.findById(id).get();
        ClassMaterial classMaterial = new ClassMaterial();
        classMaterial.setTitle(noticeData.getTitle());
        classMaterial.setContent(noticeData.getContent());
        classMaterial.setInstructorName(findInstructor.getInstructorName());
        classMaterial.setType("공지사항");
        classMaterial.setUploadDate(LocalDateTime.now());
        classMaterial.setProgram(program);

        ClassMaterial save = materialRepository.save(classMaterial);

        List<ProgramParticipantStudent> participantProgram111 = participantProgramRepository.findParticipantProgram111(program);

        for(ProgramParticipantStudent pp : participantProgram111) {
            MaterialStateManagement materialStateManagement = new MaterialStateManagement();
            materialStateManagement.setClassMaterial(save);
            materialStateManagement.setRead(false);
            materialStateManagement.setProgram(program);
            materialStateManagement.setParticipantStudent(pp);
            materialStateRepository.save(materialStateManagement);
        }


        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", classMaterial));
    }

    //공지등록
    @GetMapping("/program/{id}")
    public ResponseEntity<?> b(@PathVariable Long id) {
        log.info("공지공지: {}", id);

        Long id1 = Long.valueOf(id);
        Program program = programRepository.findById(id).get();


        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", program));
    }

}
