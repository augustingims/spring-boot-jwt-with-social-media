package cm.skysoft.app.service;

import cm.skysoft.app.criteria.AgencyUserCriteria;
import cm.skysoft.app.criteria.UserCriteriaDTO;
import cm.skysoft.app.criteria.VisitCriteriaDTO;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.domain.Visits;
import cm.skysoft.app.dto.DashboardVisitDTO;
import cm.skysoft.app.dto.VisitDTO;
import cm.skysoft.app.service.beans.DashboardVisitBeans;
import io.undertow.util.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/12/21.
 */
public interface VisitsService {

    /**
     * save visit.
     *
     * @param visit the visit
     */
    Visits save(VisitDTO visit);

    /**
     * saveVisitPrepa visit.
     *
     * @param visit the visit
     */
    Visits saveVisitPrepa(VisitDTO visit, String codeClient);

    /**
     * saveVisitBureau visit.
     *
     * @param visit the visit
     */
    Visits saveVisitBureau(VisitDTO visit, String codeClient) throws BadRequestException;

    /**
     * change status from the visit
     *
     * @param idVisit by visit
     */
    Visits validateVisit(Long idVisit);

    /**
     * update visit.
     *
     * @param visit the visit
     */
    Visits update(VisitDTO visit);

    /**
     * get visit by id.
     *
     * @param id the id
     * @return visit found.
     */
    Optional<Visits> getVisitById(Long id);

    /**
     * get visit by id.
     *
     * @param id the id
     * @return visit found.
     */
    Visits findVisitById(Long id);

    /**
     * get all Visits.
     *
     * @param pageable the pageable
     * @return list of visit found.
     */
    Page<Visits> getAllVisits(Pageable pageable);

    /**
     * get all visit.
     *
     * @return list of visit found.
     */
    List<Visits> getAllVisits();

    /**
     * delete visit.
     *
     * @param visit the visit
     */
    void delete(Visits visit);

    /**
     * delete visit by Id.
     *
     * @param id the id
     */
    void deleteById(Long id);

    /**
     * get list visit by participants
     */
    Page<Visits> getListVisitByParticipant(Long idClient, String typeVisit, LocalDateTime dateDebut,
                                           LocalDateTime dateFin, Boolean status, Pageable pageable);

    /**
     * get number of visits to prepare.
     *
     * @return number of visits to prepare.
     */
    Long getNumberVisitToPreparate(User user);

    /**
     * get number of visits to planned.
     *
     * @return number of visits to planned.
     */
    Long getNumberVisitToPlanned(User user);

    /**
     * get number of visits user created to prepare.
     *
     * @return number of visits user created to prepare.
     */
    Long getNumberVisitCreatedToPreparate(User user);

    /**
     * get number of visits user for executed.
     *
     * @return number of visits user for executed.
     */
    Long getNumberVisitForExecuted(User user);

    /**
     * get number of visits to executed.
     *
     * @return number of visits to executed.
     */
    Long getNumberVisiteToExcecuted(User user);

    /**
     * find One visit by codeVisit
     *
     * @param codeVisit of the visit
     */
    Visits findVisitByCodeVisit(String codeVisit);

    /**
     * get all Visit with specification by currentUser
     *
     * @param visitCriteria of the visitCriteria
     * @param pageable      of the pagination
     * @return list visit
     */
    Page<Visits> getAllVisit(VisitCriteriaDTO visitCriteria, Pageable pageable);

    /**
     * get all Visit for the preparation with specification
     *
     * @param visitCriteria of the visitCriteria
     * @param pageable      of the pagination
     * @return list visit
     */
    Page<Visits> getAllVisitPreparate(VisitCriteriaDTO visitCriteria, Pageable pageable);

    /**
     * comparison between the date of the visit and the local date
     */
    void getBeetwenDateVisit();

    /**
     * change in the status of the preparation
     *
     * @param idVisit by visit
     */
    void updatePreparationStatus(Long idVisit);

    /**
     * get number total visit
     *
     * @param dateBefore by dateBefore
     * @param dateAfter  by dateAfter
     */
    DashboardVisitDTO getNumberTotalVisit(String dateBefore, String dateAfter, String periode);

    /**
     * get number total visit for the users supervised
     *
     * @param dateBefore by dateBefore
     * @param dateAfter  by dateAfter
     */
    List<DashboardVisitBeans> getNumberTotalVisitForUserSupervised(String dateBefore, String dateAfter, String periode, AgencyUserCriteria agencyUserCriteria);

    /**
     * get number total visit for the users supervised
     *
     * @param dateBefore by dateBefore
     * @param dateAfter  by dateAfter
     */
    DashboardVisitBeans getTotalVisitForUserSupervised(String dateBefore, String dateAfter, String periode, UserCriteriaDTO userCriteriaDTO);
}
