package database.back.repository;

import database.back.domain.Instructor;
import database.back.domain.Program;
import database.back.domain.Student;
import database.back.dto.program.ResponseProgramDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    @Query("select p " +
            "from Program p " +
            "inner join ProgramParticipantStudent pps on p = pps.program " +
            "where pps.student = :student")
    List<Program> findParticipantProgram(Student student);


    @Query("select p from Program p where p.instructor = :instructor")
    List<Program> findProgram(Instructor instructor);

}
