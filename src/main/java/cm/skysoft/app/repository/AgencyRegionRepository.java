package cm.skysoft.app.repository;

import cm.skysoft.app.domain.AgencyRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgencyRegionRepository extends JpaRepository<AgencyRegion, Long> {

    AgencyRegion findByIdAgencyRegion(Long idAgencyRegion);

    List<AgencyRegion> findAllByOrderByAgencyRegionName();
}
