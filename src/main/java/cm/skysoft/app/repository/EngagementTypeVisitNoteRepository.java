package cm.skysoft.app.repository;

import cm.skysoft.app.domain.EngagementType;
import cm.skysoft.app.domain.EngagementTypeVisitNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EngagementTypeVisitNoteRepository extends JpaRepository<EngagementTypeVisitNote, Long> {

    @Query("select e from EngagementTypeVisitNote e where e.visitNote.id = :visitNoteId")
    List<EngagementTypeVisitNote> findEngagementTypeVisitNoteByVisitNoteId(@Param("visitNoteId") Long visitNoteId);

    @Query("select e.engagementType from EngagementTypeVisitNote e where e.visitNote.id = :visitNoteId")
    List<EngagementType> findEngagementTypeByVisitNoteId(@Param(value = "visitNoteId") Long visitNoteId);
}
