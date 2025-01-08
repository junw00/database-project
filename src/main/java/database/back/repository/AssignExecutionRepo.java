package database.back.repository;

import database.back.domain.Assignment;
import database.back.domain.AssignmentExecution;
import database.back.domain.ProgramParticipantStudent;
import database.back.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignExecutionRepo extends JpaRepository<AssignmentExecution, Long> {

    @Query("select ae from AssignmentExecution ae where ae.assignment = :assignment and ae.participantStudent = :student")
    AssignmentExecution findAssignmentExecution(Assignment assignment, ProgramParticipantStudent student);


}

