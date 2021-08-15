package cm.skysoft.app.service;

import cm.skysoft.app.domain.VisitParticipants;
import cm.skysoft.app.dto.VisitParticipantDTO;
import cm.skysoft.app.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 3/2/21.
 */
public interface VisitParticipantsService {


    /**
     * save visitParticipant.
     * @param visitParticipant the visitParticipant
     * @return list of visitParticipant found.
     */
    VisitParticipants save(VisitParticipants visitParticipant);

    /**
     * update visitParticipant.
     * @param visitParticipant the visitParticipant
     */
    void update(VisitParticipants visitParticipant);

    /**
     * get visitParticipant by id.
     * @param id the id
     * @return visitParticipant found.
     */
    Optional<VisitParticipants> getVisitParticipantById(Long id);

    /**
     * get visitParticipant by id.
     * @param idVisitParticipant the idVisitParticipant
     * @return visitParticipant found.
     */
    VisitParticipants findVisitParticipantById(Long idVisitParticipant);

    /**
     * get all visitParticipant.
     * @param pageable the pageable
     * @return list of visitParticipant found.
     */
    Page<VisitParticipants> getVisitParticipants(Pageable pageable);

    /**
     * get all visitParticipant.
     * @return list of visitParticipant found.
     */
    List<VisitParticipants> getAllVisitParticipants();

    /**
     * get all UserParticipant.
     * @param idVisit
     * @return list of UserDTO found.
     */
    List<UserDTO> getAllUserParticipants(Long idVisit);

    /**
     * get visitParticipants by idVisit.
     * @param idVisit
     * @return list of visitParticipant found.
     */
    List<VisitParticipants> getVisitParticipantsByIdVisit(Long idVisit);

    /**
     * delete visitParticipant.
     * @param visitParticipant the visitParticipant
     */
    void delete(VisitParticipantDTO visitParticipant);

    /**
     * delete visitParticipants.
     * @param visitParticipants the visitParticipant list.
     */
    void deleteAll(List<VisitParticipants> visitParticipants);

    /**
     * delete visitParticipant by Id.
     * @param id the id
     */
    void deleteById(Long id);
}
