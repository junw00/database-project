package database.back.repository;

import database.back.domain.Assignment;
import database.back.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignRepository extends JpaRepository<Assignment, Long> {

    @Query("select a from Assignment a where a.program = :program")
    List<Assignment> findAssignments(Program program);


}
