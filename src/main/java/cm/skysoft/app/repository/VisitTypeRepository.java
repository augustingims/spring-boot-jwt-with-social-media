package cm.skysoft.app.repository;

import cm.skysoft.app.domain.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Daniel 02/03/2021
 */

public interface VisitTypeRepository extends JpaRepository<VisitType, Long> {

    Optional<VisitType> findVisitTypeByIdVisitType(Long idVisitType);
}
