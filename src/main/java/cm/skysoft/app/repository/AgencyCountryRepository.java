package cm.skysoft.app.repository;

import cm.skysoft.app.domain.AgencyCountry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyCountryRepository extends JpaRepository<AgencyCountry, Long> {
    AgencyCountry findByIdAgencyCountry(Long idAgencyCountry);
}
