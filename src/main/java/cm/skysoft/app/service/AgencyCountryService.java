package cm.skysoft.app.service;

import cm.skysoft.app.domain.AgencyCountry;
import cm.skysoft.app.dto.AgencyCountryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/12/21.
 */
public interface AgencyCountryService {

    /**
     * save AgencyCountry.
     * @param agencyCountry the agencyCountry.
     * @return list of agency found.
     */
    AgencyCountry save(AgencyCountryDTO agencyCountry);

    /**
     * update agencyCountry.
     * @param agencyCountry the agencyCountry.
     */
    void update(AgencyCountryDTO agencyCountry);

    /**
     * get agencyCountry by id.
     * @param id the id.
     * @return agencyCountry found.
     */
    Optional<AgencyCountryDTO> getAgencyCountryById(Long id);

    /**
     * get agencyCountry by idAgencyCountry.
     * @param idAgencyCountry the idAgencyCountry.
     * @return agencyCountry found.
     */
    AgencyCountry getAgencyCountryByIdAgencyCountry(Long idAgencyCountry);

    /**
     * get all agencies.
     * @param pageable the pageable.
     * @return list of AgencyCountry found.
     */
    Page<AgencyCountryDTO> getAllAgenciesCountry(Pageable pageable);

    /**
     * get all agencyCountries.
     * @return list of agencyCountry found.
     */
    List<AgencyCountryDTO> getAllAgenciesCountry();

    /**
     * delete agencyCountry.
     * @param agencyCountry the AgencyCountryDTO.
     */
    void delete(AgencyCountryDTO agencyCountry);

    /**
     * delete agencyCountry by Id.
     * @param id the id.
     */
    void deleteById(Long id);
}
