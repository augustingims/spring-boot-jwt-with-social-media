package cm.skysoft.app.repository;

import cm.skysoft.app.domain.AgencyCity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by daniel on 2/12/21.
 */
public interface AgencyCityRepository extends JpaRepository<AgencyCity, Long> {
    AgencyCity findByIdAgencyCity(Long idAgencyCity);
}
