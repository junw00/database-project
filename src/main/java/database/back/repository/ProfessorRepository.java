package database.back.repository;

import database.back.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query("select p from Professor p where p.professorNum = :professorNum")
    Optional<Professor> findByProfessorNum(String professorNum);
}
