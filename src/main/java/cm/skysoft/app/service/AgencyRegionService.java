package cm.skysoft.app.service;

import cm.skysoft.app.domain.AgencyRegion;
import cm.skysoft.app.dto.AgencyRegionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by daniel on 2/12/21.
 */
public interface AgencyRegionService {
    /**
     * save agencyRegion.
     * @param agencyRegion the agencyRegion
     * @return list of agencyRegion found.
     */
   AgencyRegion save(AgencyRegionDTO agencyRegion);

    /**
     * update agencyRegion.
     * @param agencyRegion the agencyRegion
     */
    void update(AgencyRegionDTO agencyRegion);

    /**
     * get agencyRegion by id.
     * @param id the id
     * @return agencyRegion found.
     */
   Optional<AgencyRegionDTO> getAgencyRegionById(Long id);

    /**
     * get agencyRegion by idAgencyRegion.
     * @param idAgencyRegion the id
     * @return agencyRegion found.
     */
    AgencyRegion getAgencyRegionByIdAgencyRegion(Long idAgencyRegion);

    /**
     * get all agencies.
     * @param pageable the pageable
     * @return list of agencyRegion found.
     */
    Page<AgencyRegionDTO> getAllAgencyRegions(Pageable pageable);

    /**
     * get all agencies.
     * @return list of agency found.
     */
    List<AgencyRegionDTO> getAllAgencyRegions();

    /**
     * delete agencyRegion.
     * @param agencyRegion the agencyRegion
     */
    void delete(AgencyRegionDTO agencyRegion);

    /**
     * delete agencyRegion by Id.
     * @param id the id
     */
    void deleteById(Long id);
}
