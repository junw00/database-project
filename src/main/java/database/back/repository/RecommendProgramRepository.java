package database.back.repository;


import database.back.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendProgramRepository extends JpaRepository<RecommendProgram, Long> {

    @Query("select rp " +
            "from RecommendProgram rp " +
            "where rp.student = :student and rp.year = :year and rp.semester = :semester and rp.recommend = :recommend")
    RecommendProgram findRecommendProgram(Student student, String year, String semester, Recommend recommend);


    //추천 횟수 찾기
    @Query("select rp from RecommendProgram rp where rp.student = :student and rp.year = :year and rp.semester = :semester")
    List<RecommendProgram> countRecommendProgram(Student student, String year, String semester);

    @Query("select rp from RecommendProgram rp inner join Recommend r on rp.recommend = r " +
            "where rp.year = :year and rp.semester = :semester and rp.student = :student and r.program = :program")
    RecommendProgram findRecommendProgram(String year, String semester, Student student, Program program);

    @Query("select rp " +
            "from RecommendProgram rp " +
            "inner join Recommend r on r = rp.recommend" +
            " and r.professor = :professor and rp.student = :student and rp.semester = :semester and rp.year = :year and r.program = :program")
    RecommendProgram findRecommendProgram(Program program, Professor professor, Student student, String semester, String year);


    @Query("select rp " +
            "from RecommendProgram rp " +
            "inner join Recommend r on r = rp.recommend" +
            " and r.professor = :professor and rp.student = :student and rp.semester = :semester and rp.year = :year ")
    List<RecommendProgram> findRecommendProgram(Professor professor, Student student, String semester, String year);

    @Query("select rp from RecommendProgram rp inner join Recommend r on rp.recommend = r where r.professor = :professor")
    List<RecommendProgram> findRecommendState(Professor professor);

    @Query("select rp from RecommendProgram rp where rp.recommend.id = :recommendId and rp.student.studentNum = :student and rp.recommend.professor = :professor")
    RecommendProgram findRecommendProgramByRecommendIdAndStudent(Long recommendId, String student, Professor professor);

}
