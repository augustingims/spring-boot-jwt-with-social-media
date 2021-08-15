package cm.skysoft.app.service;

import cm.skysoft.app.domain.EngagementType;

import java.util.List;
import java.util.Optional;

public interface EngagementTypeService {

    /**
     * save engagement type in the db
     */
    void save();

    /**
     * get type of commitment
     * @param idEngagementTypeAfb by idEngagementTypeAfb
     * @return type of commitment
     */
    Optional<EngagementType> findEngagementTypeByIdEngagementAfb(Long idEngagementTypeAfb);

    /**
     * get list type of commitment
     * @return list type of commitment
     */
    List<EngagementType> findAll();
}
