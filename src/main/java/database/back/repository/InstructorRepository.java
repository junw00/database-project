package database.back.repository;

import database.back.domain.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {


    @Query("select i from Instructor i where i.instructorNum = :instructorNum")
    Optional<Instructor> findInstructor(String instructorNum);
}
