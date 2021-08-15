package cm.skysoft.app.repository;

import cm.skysoft.app.domain.MeansUsedForVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeansUsedForVisitReposiroty extends JpaRepository<MeansUsedForVisit, Long> {

    Optional<MeansUsedForVisit> findMeansUsedForVisitsByIdMeansUsedAfb(Long idMeansUsedAfb);
}
