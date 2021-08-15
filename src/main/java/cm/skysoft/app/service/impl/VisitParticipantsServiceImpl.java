package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.Logs;
import cm.skysoft.app.domain.VisitParticipants;

import cm.skysoft.app.dto.VisitParticipantDTO;
import cm.skysoft.app.mapper.UserMapper;
import cm.skysoft.app.service.LogsService;
import cm.skysoft.app.service.dto.UserDTO;

import cm.skysoft.app.repository.VisitParticipantsRepository;
import cm.skysoft.app.service.UserService;
import cm.skysoft.app.service.VisitParticipantsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 3/2/21.
 */
@Service
public class VisitParticipantsServiceImpl implements VisitParticipantsService {


    private final VisitParticipantsRepository visitParticipantsRepository;
    private final UserMapper userMapper;
    private final UserService userService;
    private final LogsService logsService;

    private String query = "";

    public VisitParticipantsServiceImpl(VisitParticipantsRepository visitParticipantsRepository, UserMapper userMapper, UserService userService, LogsService logsService) {
        this.visitParticipantsRepository = visitParticipantsRepository;
        this.userMapper = userMapper;
        this.userService = userService;
        this.logsService = logsService;
    }


    /**
     * save visitParticipant.
     *
     * @param visitParticipant the visitParticipant
     * @return list of visitParticipant found.
     */
    @Override
    public VisitParticipants save(VisitParticipants visitParticipant) {

        logsService.save(new Logs(userService.getCurrentUser(), "Engistrement des participants pour la visite", Logs.ENREGISTREMENT_PARTICIPANT));

        return visitParticipantsRepository.save(visitParticipant);
    }

    /**
     * update visitParticipant.
     *
     * @param visitParticipant the visitParticipant
     */
    @Override
    public void update(VisitParticipants visitParticipant) {
        visitParticipantsRepository.save(visitParticipant);
    }

    /**
     * get visitParticipant by id.
     *
     * @param id the id
     * @return visitParticipant found.
     */
    @Override
    public Optional<VisitParticipants> getVisitParticipantById(Long id) {
        return visitParticipantsRepository.findById(id);
    }

    /**
     * get visitParticipant by id.
     *
     * @param idVisitParticipant the idVisitParticipant
     * @return visitParticipant found.
     */
    @Override
    public VisitParticipants findVisitParticipantById(Long idVisitParticipant) {
        return visitParticipantsRepository.getOne(idVisitParticipant);
    }

    /**
     * get all visitParticipant.
     *
     * @param pageable the pageable
     * @return list of visitParticipant found.
     */
    @Override
    public Page<VisitParticipants> getVisitParticipants(Pageable pageable) {
        return visitParticipantsRepository.findAll(pageable);
    }

    /**
     * get all visitParticipant.
     *
     * @return list of visitParticipant found.
     */
    @Override
    public List<VisitParticipants> getAllVisitParticipants() {
        return visitParticipantsRepository.findAll();
    }

    /**
     * get all UserParticipant.
     *
     * @param idVisit
     * @return list of UserDTO found.
     */
    @Override
    public List<UserDTO> getAllUserParticipants(Long idVisit) {
        return userMapper.usersToUserDTOs(visitParticipantsRepository.getAllUserParticipants(idVisit));
    }

    /**
     * get visitParticipants by idVisit.
     *
     * @param idVisit
     * @return list of visitParticipant found.
     */
    @Override
    public List<VisitParticipants> getVisitParticipantsByIdVisit(Long idVisit) {
        return visitParticipantsRepository.getVisitParticipantsByIdVisit(idVisit);
    }


    /**
     * delete visitParticipant.
     *
     * @param visitParticipant the visitParticipant
     */
    @Override
    public void delete(VisitParticipantDTO visitParticipant) {
        VisitParticipants visitParticipants = findVisitParticipantById(visitParticipant.getId());
        visitParticipantsRepository.delete(visitParticipants);
    }

    /**
     * delete visitParticipants.
     *
     * @param visitParticipants the visitParticipant list.
     */
    @Override
    public void deleteAll(List<VisitParticipants> visitParticipants) {
        visitParticipantsRepository.deleteAll(visitParticipants);
    }


    /**
     * delete visitParticipant by Id.
     *
     * @param id the id
     */
    @Override
    public void deleteById(Long id) {
        visitParticipantsRepository.deleteById(id);
    }


}
