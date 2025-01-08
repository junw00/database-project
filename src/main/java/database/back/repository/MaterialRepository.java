package database.back.repository;

import database.back.domain.ClassMaterial;
import database.back.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<ClassMaterial, Long> {

    @Query("select c from ClassMaterial c where c.program = :program")
    List<ClassMaterial> findClassMaterial(Program program);

    @Query("select c from ClassMaterial c where c.program.id = :id")
    List<ClassMaterial> findClassMaterial(Long id);
}
