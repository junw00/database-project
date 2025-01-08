package database.back.service;
import database.back.domain.Program;
import database.back.dto.AttendanceSummaryDTO;
import database.back.dto.program.ResponseProgramDto;
import database.back.repository.AttendanceRepository;
import database.back.repository.ProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final ProgramRepository programRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, ProgramRepository programRepository) {
        this.attendanceRepository = attendanceRepository;
        this.programRepository = programRepository;
    }

    public List<AttendanceSummaryDTO> getAttendanceSummary(Long programId) {
        return attendanceRepository.findAttendanceSummaryByProgramId(programId);
    }

    public Long getTotalStudents(Long programId) {
        return attendanceRepository.countDistinctStudentsByProgramId(programId);
    }

//    public ResponseProgramDto getProgramInfo(Long programId) {
//        Program program = programRepository.findById(programId)
//                .orElseThrow(() -> new EntityNotFoundException("Program not found"));
//        Long totalStudents = getTotalStudents(programId);
//        return new ResponseProgramDto(program.getId(), program.getProgramTitle(), totalStudents);
//    }
}