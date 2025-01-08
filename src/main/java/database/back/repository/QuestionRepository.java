package database.back.repository;

import database.back.domain.Assignment;
import database.back.domain.Question;
import database.back.dto.assignment.responseDto.ResponseQuestionDto;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

//@Repository
//public interface QuestionRepository extends JpaRepository<Question, Long> {
//    @Query(value = "SELECT q.question_id as questionId, q.question_content as questionContent, " +
//            "p.example_content as exampleContent, p.is_correct as isCorrect " +
//            "FROM Question q " +
//            "INNER JOIN Example p ON q.question_id = p.question_id " +
//            "WHERE q.assignment_id = :assignmentId " +
//            "ORDER BY RAND() LIMIT 5",
//            nativeQuery = true)


//    @Query("select q from Question q inner join Example e on q.where q.assignment = :assignment")
//    List<ResponseQuestionDto> findRandomQuestionsAndExamples(Assignment assignment);
//
//}

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

//    @Query(value = "SELECT q.question_id as questionId, q.question_content as questionContent, " +
//            "e.example_content as exampleContent, e.is_correct as isCorrect " +
//            "FROM Question q " +
//            "INNER JOIN Example e ON q.question_id = e.question_id " +
//            "WHERE q.assignment_id = :assignmentId " +
//            "ORDER BY RAND() LIMIT 5",
//            nativeQuery = true)
//    List<Object[]> findRandomQuestionsAndExamples(@Param("assignmentId") Long assignmentId);

    @Query(value = "SELECT * FROM Question q WHERE q.assignment_id = :assignmentId ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Question> findRandomByAssignment(@Param("assignmentId") Long assignmentId);


}
