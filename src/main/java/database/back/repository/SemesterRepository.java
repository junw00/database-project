package database.back.repository;

import database.back.domain.SemesterSpecificStuInfo;
import database.back.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<SemesterSpecificStuInfo, Long> {
    @Query("select sp from SemesterSpecificStuInfo sp where sp.student = :student and sp.year = :year and sp.semester = :semester")
    SemesterSpecificStuInfo findSemesterSpecificStuInfo(Student student, String year, String semester);
}
