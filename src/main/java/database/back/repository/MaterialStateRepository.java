package database.back.repository;

import database.back.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialStateRepository extends JpaRepository<MaterialStateManagement, Long> {

    @Query("select ms from " +
            "MaterialStateManagement ms " +
            "where ms.participantStudent = :student and ms.program = :program")
    List<MaterialStateManagement> findMaterialStateManagement(ProgramParticipantStudent student, Program program);

    @Query("select ms from MaterialStateManagement ms where ms.participantStudent = :ps and ms.classMaterial = :cm")
    MaterialStateManagement findMaterialState(ProgramParticipantStudent ps, ClassMaterial cm);

    @Query("select ms from MaterialStateManagement ms where ms.classMaterial = :classMaterial and ms.participantStudent = :student")
    MaterialStateManagement findMaterialStateManagement(ClassMaterial classMaterial, ProgramParticipantStudent student);
}
