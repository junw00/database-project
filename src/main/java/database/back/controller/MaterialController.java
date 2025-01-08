package database.back.controller;

import database.back.domain.*;
import database.back.dto.ResponseDto;
import database.back.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MaterialController {
    private final MaterialRepository materialRepository;
    private final ParticipantProgramRepository participantProgramRepository;
    private final MaterialStateRepository materialStateRepository;
    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;

//    public ResponseEntity<?> getMaterialState(HttpSession httpSession) {
//        Student student = (Student) httpSession.getAttribute("userInfo");
//        List<Program> participantProgram = programRepository.findParticipantProgram(student);
//
//
//    }

    @GetMapping("/material/{id}")
    public ResponseEntity<?> getClassMaterial(@PathVariable Long id, HttpSession session) {
        log.info("id: {}", id);
        List<ClassMaterial> classMaterial = materialRepository.findClassMaterial(id);
        Student student = (Student) session.getAttribute("userInfo");
//        studentRepository.findById(student.get)
        ProgramParticipantStudent participantProgram = participantProgramRepository.findParticipantProgram(student.getStudentNum(), id);

        for (ClassMaterial c: classMaterial) {
            MaterialStateManagement materialState = materialStateRepository.findMaterialState(participantProgram, c);
            if (!materialState.isRead()) {
                c.setRead("안읽음");
            }
        }

        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", classMaterial));
    }

    @GetMapping("material/detail/{id}/{materialId}")
    @Transactional
    public ResponseEntity<?> getMaterialDetail(@PathVariable Long id, @PathVariable Long materialId, HttpSession session) {
        log.info("id: {}", id);
        ClassMaterial classMaterial = materialRepository.findById(materialId).get();
        log.info("classMaterial: {}", classMaterial);
        Student student = (Student) session.getAttribute("userInfo");

        ProgramParticipantStudent participantProgram = participantProgramRepository.findParticipantProgram(student.getStudentNum(), id);
        MaterialStateManagement materialStateManagement = materialStateRepository.findMaterialStateManagement(classMaterial , participantProgram);
        materialStateManagement.setRead(true);
        log.info("materialStateManagement: {}", materialStateManagement);

        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", classMaterial));
    }

}
