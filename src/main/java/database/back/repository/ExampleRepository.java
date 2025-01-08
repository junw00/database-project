package database.back.repository;

import database.back.domain.Example;
import database.back.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {

    @Query("select e from Example e where e.question = :question")
    List<Example> findExampleByQuestion(Question question);
}
