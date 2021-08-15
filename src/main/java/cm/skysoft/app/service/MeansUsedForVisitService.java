package cm.skysoft.app.service;

import cm.skysoft.app.domain.MeansUsedForVisit;

import java.util.List;
import java.util.Optional;

public interface MeansUsedForVisitService {

    /**
     * save means used for the visit
     */
    void save();

    /**
     * get means used for the visit
     * @param idMeansUsedAfb the means used
     * @return means used for the visit
     */
    Optional<MeansUsedForVisit> findMeansUsedForVisit(Long idMeansUsedAfb);

    /**
     * get list means used for the visit
     * @return list means used for the visit
     */
    List<MeansUsedForVisit> findAll();

}
