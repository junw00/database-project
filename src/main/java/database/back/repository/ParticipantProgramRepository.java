package database.back.repository;

import database.back.domain.Program;
import database.back.domain.ProgramParticipantStudent;
import database.back.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantProgramRepository extends JpaRepository<ProgramParticipantStudent, Long> {

    @Query("select pp from ProgramParticipantStudent pp where pp.student = :student")
    List<ProgramParticipantStudent> findParticipantProgram(Student student);

    @Query("select pp from ProgramParticipantStudent pp where pp.recommendId = :id")
    List<ProgramParticipantStudent> findTeam(Long id);

    @Query("select pp from ProgramParticipantStudent pp where pp.student.studentNum = :studentNum and pp.program.id = :id")
    ProgramParticipantStudent findParticipantProgram(String studentNum, Long id);

    @Query("select pp from ProgramParticipantStudent pp where pp.program = :program")
    List<ProgramParticipantStudent> findParticipantProgram111(Program program);
}
