package database.back.repository;

import database.back.domain.Professor;
import database.back.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    @Query("select s from Student s where s.professor = :professor")
    List<Student> findStudentsByProfessor(Professor professor);


}
