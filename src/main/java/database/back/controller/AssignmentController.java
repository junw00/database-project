package database.back.controller;

import database.back.domain.*;
import database.back.dto.ExampleData;
import database.back.dto.ResponseDto;
import database.back.dto.assignment.requestDto.AssignmentRequestDto;
import database.back.dto.assignment.requestDto.RequestQuestionDto;
import database.back.dto.assignment.responseDto.AssignmentsResponseDto;
import database.back.dto.question.ResponseQuestionDto;
import database.back.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignRepository assignRepository;
    private final ProgramRepository programRepository;
    private final QuestionRepository questionRepository;
    private final ExampleRepository exampleRepository;
    private final AssignExecutionRepo assignExecutionRepo;
    private final ParticipantProgramRepository participantProgramRepository;
    private final StudentRepository studentRepository;

    //과제 등록
    @PostMapping("/assignments/{id}")
    public ResponseEntity<?> c(HttpSession session, @PathVariable Long id, @RequestBody AssignmentRequestDto assignmentRequestDto ) {
        Instructor instructor = (Instructor) session.getAttribute("instructor");
        assignmentRequestDto.setDeadline(assignmentRequestDto.getDeadline().plusDays(1));
        Program program = programRepository.findById(id).get();
        Assignment assignment = AssignmentRequestDto.uploadAssignment(assignmentRequestDto, program, instructor, assignmentRequestDto.getAssignmentTitle());
        Assignment save = assignRepository.save(assignment);

        List<RequestQuestionDto> questions = assignmentRequestDto.getQuestions();

        for(RequestQuestionDto questionDto: questions) {
            Question question = new Question();
            question.setAssignment(save);
            question.setQuestionContent(questionDto.getQuestion());
            Question saveQuestion = questionRepository.save(question);
            List<String> options = questionDto.getOptions();
            for(int i = 0; i < options.size(); i++) {
                Example example = new Example();
                example.setExampleContent(options.get(i));
                example.setCorrect(questionDto.getAnswer() == i + 1);
                example.setQuestion(saveQuestion);
                exampleRepository.save(example);
            }
        }
        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", true));
    }

    @GetMapping("/assignments/{id}")
    public ResponseEntity<?> getAssignments(@PathVariable Long id, HttpSession session) {
        Program program = programRepository.findById(id).get();
        List<Assignment> assignments = assignRepository.findAssignments(program);
        Student student = (Student) session.getAttribute("userInfo");
        ProgramParticipantStudent participantProgram = participantProgramRepository.findParticipantProgram(student.getStudentNum(), id);

        for(Assignment a : assignments) {
            AssignmentExecution assignmentExecution = assignExecutionRepo.findAssignmentExecution(a, participantProgram);

            if (assignmentExecution != null && assignmentExecution.getIncorretNum() == 0) {
                a.setCompleted(true);
            }else {
                a.setCompleted(false);
            }
        }

        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", assignments));
    }

    @PostMapping("/submit/{id}/{assignment}")
    @Transactional
    public ResponseEntity<?> submitAssignment(@RequestBody List<ExampleData> exampleData, HttpSession session, @PathVariable Long id,@PathVariable Long assignment) {
        AssignmentExecution assignmentExecution = new AssignmentExecution();
        Student student = (Student) session.getAttribute("userInfo");
        Student findStudent = studentRepository.findById(student.getStudentNum()).get();
        ProgramParticipantStudent participantProgram = participantProgramRepository.findParticipantProgram(findStudent.getStudentNum(), id);
        Assignment assignment1 = assignRepository.findById(assignment).get();



        AssignmentExecution assignmentExecution1 = assignExecutionRepo.findAssignmentExecution(assignment1, participantProgram);
        int totalCount = exampleData.size();
        int correct = 0;
        int inCorrect = 0;
        if(assignmentExecution1 == null) {

            for (ExampleData ed : exampleData) {
                if(ed.isCorrect()) correct += 1;
                else inCorrect += 1;
            }
            if(inCorrect == 0) {

                assignmentExecution.setAssignmentCompletion(true);

            }else  {
                assignmentExecution.setAssignmentCompletion(false);
            }

            assignmentExecution.setExecutionDate(LocalDateTime.now());
            assignmentExecution.setAssignment(assignment1);
            assignmentExecution.setCorrectNum(correct);
            assignmentExecution.setParticipantStudent(participantProgram);
            assignmentExecution.setIncorretNum(inCorrect);
            assignExecutionRepo.save(assignmentExecution);
        }else {

            for (ExampleData ed : exampleData) {
                if(ed.isCorrect()) correct += 1;
                else inCorrect += 1;
            }if(inCorrect == 0) {
                assignmentExecution1.setAssignmentCompletion(true);
            }else {
                assignmentExecution1.setAssignmentCompletion(false);
            }

            assignmentExecution1.setExecutionDate(LocalDateTime.now());
            assignmentExecution1.setAssignment(assignment1);
            assignmentExecution1.setCorrectNum(correct);
            assignmentExecution1.setParticipantStudent(participantProgram);
            assignmentExecution1.setIncorretNum(inCorrect);
        }
        return ResponseEntity.ok().body(new ResponseDto<>(200, "제출","제출 성공"));
    }

    @GetMapping("/program/{id}/assignment/{assignmentId}")
    public ResponseEntity<?> getRandomQuestions(@PathVariable Long id, @PathVariable Long assignmentId) {
        Assignment assignment = assignRepository.findById(assignmentId).get();
        if(!assignment.getDeadlineDate().isAfter(LocalDateTime.now())) {
            return ResponseEntity.ok().body(new ResponseDto<>(500, "시간 지남", null));
        }
        Program program = programRepository.findById(id).get();
        List<Question> findQuestions = questionRepository.findRandomByAssignment(assignmentId);

        List<ResponseQuestionDto> questionDtos = new ArrayList<>();

        for (Question q :findQuestions) {
            ResponseQuestionDto responseQuestionDto = new ResponseQuestionDto();
            responseQuestionDto.setQuestionId(q.getId());
            responseQuestionDto.setQuestionContent(q.getQuestionContent());
            List<Example> exampleByQuestion = exampleRepository.findExampleByQuestion(q);
            responseQuestionDto.setExamples(exampleByQuestion);
            questionDtos.add(responseQuestionDto);
        }

        return ResponseEntity.ok().body(new ResponseDto<>(200, "success", questionDtos));

    }
}
