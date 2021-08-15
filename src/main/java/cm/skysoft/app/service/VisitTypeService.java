package cm.skysoft.app.service;

import cm.skysoft.app.domain.VisitType;
import cm.skysoft.app.dto.VisitTypeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by Daniel 02/03/2021
 */

public interface VisitTypeService {

    /**
     * Save Visit Type
     */
    void save();

    /**
     * Update Visit type
     * @param visitTypeDTO the VisitType
     */
    VisitType update(VisitTypeDTO visitTypeDTO);

    /**
     * get List Visit type
     */
    List<VisitType> findAll();

    /**
     * get one Visit type
     * @param id the id visitType
     */
    Optional<VisitType> findOne(Long id);
}
