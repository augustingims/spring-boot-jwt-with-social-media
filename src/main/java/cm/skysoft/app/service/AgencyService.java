package cm.skysoft.app.service;

import cm.skysoft.app.domain.Agency;
import cm.skysoft.app.dto.AgencyDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/12/21.
 */

public interface AgencyService {

    /**
     * save agency.
     * @param agency the agency
     * @return list of agency found.
     */
    Agency save(Agency agency);

    /**
     * update agency.
     * @param agency the agency
     */
    void update(AgencyDTO agency);

    /**
     * get agency by id.
     * @param idAgency the idAgency
     * @return agency found.
     */
    Agency getAgencyByIdAgency(Long idAgency);

    Optional<Agency> getAgencyById(Long idAgency);
    /**
     * get all agencies.
     * @return list of agency found.
     */
    List<Agency> findAll();

    /**
     * delete agency.
     * @param agency the agency
     */
    void delete(AgencyDTO agency);

    /**
     * delete agency by Id.
     * @param id the id
     */
    void deleteById(Long id);
}
