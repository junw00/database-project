package database.back.repository;

import database.back.domain.Attendance;
import database.back.domain.ProgramParticipantStudent;
import database.back.dto.AttendanceSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("select count(a) from Attendance a where a.attendanceStatus = true and a.participantStudent = :participantStudent")
    int attendanceCount(ProgramParticipantStudent participantStudent);

    @Query("select count(a) from Attendance a where a.participantStudent = :participantStudent")
    int countParticipant(ProgramParticipantStudent participantStudent);

    @Query("SELECT new database.back.dto.AttendanceSummaryDTO(a.classDate, " +
            "COUNT(CASE WHEN a.attendanceStatus = true THEN 1 END), " +
            "COUNT(CASE WHEN a.attendanceStatus = false THEN 1 END)) " +
            "FROM Attendance a " +
            "WHERE a.program.id = :programId " +
            "GROUP BY a.classDate " +
            "ORDER BY a.classDate DESC")
    List<AttendanceSummaryDTO> findAttendanceSummaryByProgramId(@Param("programId") Long programId);

    @Query("SELECT COUNT(DISTINCT a.participantStudent.id) FROM Attendance a WHERE a.program.id = :programId")
    Long countDistinctStudentsByProgramId(@Param("programId") Long programId);
}

