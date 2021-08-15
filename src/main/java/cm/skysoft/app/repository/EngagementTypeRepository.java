package cm.skysoft.app.repository;

import cm.skysoft.app.domain.EngagementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EngagementTypeRepository extends JpaRepository<EngagementType, Long> {

    Optional<EngagementType> findEngagementTypeByIdEngagementTypeAfb(Long id);
}
