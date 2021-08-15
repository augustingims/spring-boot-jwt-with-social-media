package cm.skysoft.app.repository;

import cm.skysoft.app.domain.VisitReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 4/26/21.
 */
public interface VisitReportRepository extends JpaRepository<VisitReport, Long> {

    @Query("SELECT vr From VisitReport vr WHERE vr.visitNote.visits.visitCode =:visitCode")
    List<VisitReport> getVisitReportsByVisitCode(@Param(value = "visitCode") String visitCode);

    @Query("SELECT vr From VisitReport vr WHERE vr.visitNote.visits.visitCode =:visitCode")
    Optional<VisitReport> getVisitReportByVisitNoteVisitsVisitCode(@Param(value = "visitCode") String visitCode);
}
