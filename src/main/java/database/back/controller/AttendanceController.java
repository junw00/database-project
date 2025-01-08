//package database.back.controller;
//import database.back.domain.Program;
//import database.back.dto.AttendanceSummaryDTO;
//import database.back.dto.program.ResponseProgramDto;
//import database.back.service.AttendanceService;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/instructors/attendance")
//public class AttendanceController {
//    private final AttendanceService attendanceService;
//
//    public AttendanceController(AttendanceService attendanceService) {
//        this.attendanceService = attendanceService;
//    }
//
//    @GetMapping("/{programId}")
//    public ResponseEntity<?> getAttendanceSummary(@PathVariable Long programId) {
//        try {
//
//            List<AttendanceSummaryDTO> summary = attendanceService.getAttendanceSummary(programId);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("attendanceSummary", summary);
//
//            return ResponseEntity.ok(response);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
//        }
//    }
//
//}
