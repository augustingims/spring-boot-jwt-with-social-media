package cm.skysoft.app.service;

import cm.skysoft.app.domain.AgencyCity;
import cm.skysoft.app.dto.AgencyCityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/12/21.
 */
public interface AgencyCityService {

    /**
     * save agencyCity.
     * @param agencyCity the agencyCity.
     * @return agencyCity save.
     */
    AgencyCity save(AgencyCityDTO agencyCity);

    /**
     * update agencyCity.
     * @param agencyCity the agencyCity.
     */
    void update(AgencyCityDTO agencyCity);

    /**
     * get agencyCity by id.
     * @param id the id.
     * @return agencyCity found.
     */
    Optional<AgencyCityDTO> getAgencyCityById(Long id);

    /**
     * get agencyCity by id.
     * @param idAgencyCity the idAgencyCity
     * @return agencyCity found.
     */
    AgencyCity getAgencyCityByIdAgencyCity(Long idAgencyCity);

    /**
     * get all agencyCity.
     * @param pageable the pageable.
     * @return list of agencyCity found.
     */
    Page<AgencyCityDTO> getAllagencyCities(Pageable pageable);

    /**
     * get all agencyCity.
     * @return list of agencyCity found.
     */
    List<AgencyCityDTO> getAllagencyCities();

    /**
     * delete agencyCity.
     * @param agencyCity the agencyCity.
     */
    void delete(AgencyCityDTO agencyCity);

    /**
     * delete agencyCity by Id.
     * @param id the id.
     */
    void deleteById(Long id);
}
