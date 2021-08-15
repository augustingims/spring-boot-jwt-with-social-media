package cm.skysoft.app.repository;

import cm.skysoft.app.domain.VisitNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitNoteRepository extends JpaRepository<VisitNote, Long> {

    Optional<VisitNote> findVisitNoteById(Long id);

    Optional<VisitNote> findVisitNoteByVisitsId(Long id);

    @Query("select v from VisitNote v where v.visits.visitCode = :visitCode")
    Optional<VisitNote> findVisitNoteByVisitsVisitCode(@Param("visitCode") String visitCode);

    @Query("select v from VisitNote v where v.visits.visitCode = :visitCode")
    Optional<VisitNote> findVisitNoteByVisitsCode(@Param("visitCode") String visitCode);

    @Query("SELECT count(v) from VisitNote v WHERE year(v.dateCreated) = year(current_date()) and v.visits.client.codeClient = :codeClient")
    Long countVisitNoteByCurrentYear(@Param("codeClient") String codeClient);

    @Query("select v from VisitNote v WHERE v.visits.client.codeClient = :codeClient and v.dateCreated = (select max(v.dateCreated) from VisitNote v)")
    VisitNote getLastVisitNote(@Param("codeClient") String codeClient);

    @Query("select v from VisitNote v where v.visits.execution = TRUE and v.visits.preparation = TRUE and v.visits.planification = TRUE  AND v.visits.reporting = FALSE")
    List<VisitNote> getNumberVisiteToExcecuted();

}
