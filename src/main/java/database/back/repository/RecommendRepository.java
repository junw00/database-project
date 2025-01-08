package database.back.repository;

import database.back.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    @Query("select p " +
            "from Program p inner join Recommend r on p = r.program inner join RecommendProgram rp on rp.recommend = r " +
            "where rp.student = :student")
    List<Program> findRecommendProgram(Student student);

    @Query("select r from Recommend r where r.program = :program and r.professor = :professor")
    List<Recommend> getRecommend(Program program, Professor professor);




}
