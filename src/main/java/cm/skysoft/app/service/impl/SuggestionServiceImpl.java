package cm.skysoft.app.service.impl;

import cm.skysoft.app.criteria.SuggestionCriteriaDTO;
import cm.skysoft.app.domain.*;
import cm.skysoft.app.dto.SuggestionDTO;
import cm.skysoft.app.dto.SuggestionVisiteDTO;
import cm.skysoft.app.dto.UserDTO;
import cm.skysoft.app.mapper.SuggestionsMapper;
import cm.skysoft.app.repository.NotificationRepository;
import cm.skysoft.app.repository.SuggestionRepository;
import cm.skysoft.app.repository.VisitsRepository;
import cm.skysoft.app.repository.specification.SuggestionSpecification;
import cm.skysoft.app.service.*;
import cm.skysoft.app.utils.MethoUtils;
import cm.skysoft.app.utils.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by daniel on 2/18/21.
 */
@Service
public class SuggestionServiceImpl implements SuggestionsService {

    private final Logger LOGGER = LoggerFactory.getLogger(SuggestionServiceImpl.class);
    private final SuggestionRepository suggestionRepository;
    private final SuggestionsMapper suggestionsMapper;
    private final VisitsService visitsService;
    private final ConfigApplicationService configApplicationService;
    private final NotificationRepository notificationRepository;
    private final VisitsRepository visitsRepository;
    private final UserAfbsService userAfbsService;
    private final ClientsService clientsService;
    private final UserService userService;
    private final SuggestionParticipantService suggestionParticipantService;
    private final VisitParticipantsService visitParticipantsService;
    private final MailService mailService;

    private final EntityManager em;

    public SuggestionServiceImpl(SuggestionRepository suggestionRepository, SuggestionsMapper suggestionsMapper, VisitsService visitsService, ConfigApplicationService configApplicationService, NotificationRepository notificationRepository, VisitsRepository visitsRepository, UserAfbsService userAfbsService, ClientsService clientsService, UserService userService, SuggestionParticipantService suggestionParticipantService, VisitParticipantsService visitParticipantsService, MailService mailService, EntityManager em) {
        this.suggestionRepository = suggestionRepository;
        this.suggestionsMapper = suggestionsMapper;
        this.visitsService = visitsService;
        this.configApplicationService = configApplicationService;
        this.notificationRepository = notificationRepository;
        this.visitsRepository = visitsRepository;
        this.userAfbsService = userAfbsService;
        this.clientsService = clientsService;
        this.userService = userService;
        this.suggestionParticipantService = suggestionParticipantService;
        this.visitParticipantsService = visitParticipantsService;
        this.mailService = mailService;
        this.em = em;
    }

    /**
     * save SuggestionVisiteDTO on a visit.
     *
     * @param suggestionVisiteDTO the SuggestionVisiteDTO on a visit.
     */
    @Override
    public Suggestions saveSuggestionVisit(SuggestionVisiteDTO suggestionVisiteDTO) {

        Suggestions s = new Suggestions();

        User currentUser = userService.getCurrentUser();

        Visits v = visitsService.findVisitById(suggestionVisiteDTO.getIdVisit());
        s.setVisit(v);

        Clients c = clientsService.getClientById(suggestionVisiteDTO.getIdClient());
        s.setClients(c);

        String login = SecurityUtils.getCurrentUserLogin().orElse(null);

        Optional<User> userById = userService.getUserById(suggestionVisiteDTO.getIdUser());

        User userRecieved = userById.orElse(null);

        User u = userAfbsService.getUserAfbByIdUserAfb(suggestionVisiteDTO.getIdUser());
        s.setUserAfb(u);

        User user = userService.getUserWithAuthoritiesByLogin(login).orElse(null);
        s.setUserAfbExpediteur(user);
        s.setDateCreate(LocalDateTime.now());
        s.setMotivation(suggestionVisiteDTO.getMotivation());
        s.setDateVisit(suggestionVisiteDTO.getDateVisite());
        if(suggestionVisiteDTO.getHourVisit() != null){
            s.setHourVisit(suggestionVisiteDTO.getHourVisit());
        }
        return getSuggestions(suggestionVisiteDTO, s, userRecieved, currentUser);
    }

    @Override
    public Suggestions saveSuggestionMobile(SuggestionVisiteDTO suggestionVisiteDTO) {

        Suggestions s = new Suggestions();
        String login;
        User userRecieved = null;

        User currentUser = userService.getCurrentUser();
        Visits v = visitsService.findVisitById(suggestionVisiteDTO.getIdVisit());
        s.setVisit(v);

        Clients c = clientsService.getClientByIdClient(suggestionVisiteDTO.getIdClient());
        s.setClients(c);

        login = SecurityUtils.getCurrentUserLogin().orElse(null);

        Optional<User> userById = userService.getUserById(suggestionVisiteDTO.getIdUser());

        if(userById.isPresent()) {
            userRecieved = userById.get();
        }

        User u = userAfbsService.getUserAfbByIdUserAfb(suggestionVisiteDTO.getIdUser());
        s.setUserAfb(u);

        userService.getUserWithAuthoritiesByLogin(login).ifPresent(s::setUserAfbExpediteur);
        s.setDateCreate(LocalDateTime.now());
        s.setMotivation(suggestionVisiteDTO.getMotivation());
        s.setDateVisit(suggestionVisiteDTO.getDateVisite());
        return getSuggestions(suggestionVisiteDTO, s, userRecieved, currentUser);
    }

    @NotNull
    private Suggestions getSuggestions(SuggestionVisiteDTO suggestionVisiteDTO, Suggestions s, User userRecieved, User currentUser) {
        s.setVisitObject(suggestionVisiteDTO.getVisitObject());
        s.setMoyenUtilise(suggestionVisiteDTO.getMoyenUtilise());
        s.setTypeVisit(suggestionVisiteDTO.getTypeVisit());

        s = suggestionRepository.save(s);

        if (suggestionVisiteDTO.getParticipant() != null){

            for (UserDTO userDTO: suggestionVisiteDTO.getParticipant()) {

                Optional<User> user1 = userService.getUserById(Long.valueOf(userDTO.getId()));

                if (user1.isPresent()) {
                    SuggestionParticipants suggestionParticipants =  new SuggestionParticipants();
                    suggestionParticipants.setUser(user1.get());
                    suggestionParticipants.setSuggestion(s);
                    suggestionParticipantService.save(suggestionParticipants);
                }
            }
        }

        ConfigApplication configApplications = configApplicationService.findOne();

        if(userRecieved != null) {
            saveNotificationRepository(currentUser, userRecieved, "EMISSION DE LA SUGGESTION SUR UNE VISITE", patternNotification(configApplications != null ? configApplications.getPatternEmissionSuggestion(): "", s, currentUser));

            sendMail(userRecieved, "EMISSION DE LA SUGGESTION SUR UNE VISITE", patternNotification(configApplications != null ? configApplications.getPatternEmissionSuggestion(): "", s, currentUser));
        }

        return s;
    }

    /**
     * update suggestion on a visit.
     *
     * @param idSuggestion the suggestion on a visit.
     */
    @Override
    public void update(Long idSuggestion) {

        if(idSuggestion != null) {
            Suggestions suggestions = getSuggestionsByIdSuggestions(idSuggestion).orElse(null);

            Suggestions suggestions1 = new Suggestions();

            if(suggestions != null) {
                if (suggestions.getId() != null) {
                    suggestions1.setDateCreate(suggestions.getDateCreate());
                    suggestions1.setStatus(Boolean.TRUE);
                    suggestions1.setUserAfbExpediteur(suggestions.getUserAfbExpediteur());
                    suggestions1.setId(suggestions.getId());
                    suggestions1.setUserAfb(suggestions.getUserAfb());
                    suggestions1.setClients(suggestions.getClients());
                    suggestions1.setVisit(suggestions.getVisit());
                    suggestions1.setMotivation(suggestions.getMotivation());

                    suggestions1.setCodeSuggestion(suggestions.getCodeSuggestion());
                    suggestions1.setTypeVisit(suggestions.getTypeVisit());
                    suggestions1.setMoyenUtilise(suggestions.getMoyenUtilise());
                    suggestions1.setVisitObject(suggestions.getVisitObject());
                    suggestions1.setDateVisit(suggestions.getDateVisit());
                    if(suggestions.getHourVisit() != null) {
                        suggestions1.setHourVisit(suggestions.getHourVisit());
                    }

                    suggestionRepository.save(suggestions1);
                }
            }
        }
    }

    /**
     * get suggestion by idSuggeston.
     *
     * @param idSuggeston the idSuggeston.
     * @return suggestion found.
     */
    @Override
    public Optional<Suggestions> getSuggestionsByIdSuggestions(Long idSuggeston) {
        if(idSuggeston != null)
            return suggestionRepository.findSuggestionsById(idSuggeston);
        else
            return Optional.empty();
    }

    /**
     * get all suggestion.
     *
     * @param pageable the pageable.
     * @return list of suggestion found.
     */
    @Override
    public Page<Suggestions> getAllSuggestions(Pageable pageable) {

        Page<Suggestions> suggestionsPage = suggestionRepository.findAll(pageable);

        if(!suggestionsPage.isEmpty()){
            return suggestionsPage;
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    @Override
    public List<Suggestions> getAllSuggestionBySearch(Long idUserAfb, Long idUserAfbExpediteur, Long idClient, Long resultmax, Boolean status, String dateDebut, String dateFin) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd yyyy HH:mm:ss 'GMT 0100'", Locale.US);

        LocalDateTime dateTimeDebut = null;
        LocalDateTime dateTimeFin = null;
        if(!dateDebut.isEmpty()) {
            System.out.println("parse " + LocalDateTime.parse(dateDebut, formatter));
            dateTimeDebut = LocalDateTime.parse(dateDebut, formatter);
        }
        if(!dateFin.isEmpty()) {
            dateTimeFin = LocalDateTime.parse(dateFin, formatter);
        }

        Query q = null;

        String query = " select s from Suggestions s where 1=1 ";

        if(idClient != null){
            query += " and s.clients.idClient = :idClient ";
        }
        if(status != null){
            query += " and s.status = :status ";
        }
        if(dateTimeDebut != null){
            query += " and s.dateCreate >= :dateTimeDebut ";
        }
        if(dateTimeFin != null){
            query += " and s.dateCreate <= :dateTimeFin ";
        }
        if(dateTimeDebut != null &&  dateTimeFin != null){
            query += " and s.dateCreate between :dateTimeDebut and :dateTimeFin ";
        }
        if(idUserAfb != null){
            query += " and s.userAfb.idUserAfb = :idUserAfb ";
        }
        if(idUserAfbExpediteur != null){
            query += " and s.userAfbExpediteur.id = :idUserAfbExpediteur ";
        }

        query += " order by s.id desc ";

        q = em.createQuery(query);

        if(idClient != null){
            q.setParameter("idClient", idClient);
        }if(status != null){
            q.setParameter("status", status);
        }if(dateTimeDebut != null){
            q.setParameter("dateTimeDebut", dateTimeDebut);
        }if(dateTimeFin != null){
            q.setParameter("dateTimeFin", dateTimeFin);
        }if(idUserAfb != null){
            q.setParameter("idUserAfb", idUserAfb);
        }if(idUserAfbExpediteur != null){
            q.setParameter("idUserAfbExpediteur", idUserAfbExpediteur);
        }
        if(resultmax != null){
            q.setMaxResults(resultmax.intValue());
        }

        return q.getResultList();
    }

    /**
     * delete suggestion.
     *
     * @param suggestionDTO the suggestion
     */
    @Override
    public void delete(SuggestionDTO suggestionDTO) {
        LOGGER.debug("REQ - delete visit");
        suggestionRepository.delete(suggestionsMapper.suggestionDToTosuggestions(suggestionDTO));
    }

    /**
     * delete suggestion by Id.
     *
     * @param id the id.
     */
    @Override
    public void deleteById(Long id) {
        LOGGER.debug("REQ - delete visit by id");
        suggestionRepository.deleteById(id);
    }

    @Override
    public Long getNumberSuggestionUnread() {
        User user = userService.getCurrentUser();
        return suggestionRepository.getNumberSuggestionUnreadByUser(user.getId());
    }

    @Override
    public Suggestions findSuggestionByCodeSuggestion(String codeSuggestion) {
        return suggestionRepository.findSuggestionsByCodeSuggestion(codeSuggestion);
    }

    @Override
    public Page<Suggestions> getAllSuggestionPage(SuggestionCriteriaDTO suggestionCriteria, Pageable pageable) {
        return suggestionRepository.findAll(SuggestionSpecification.getSpecification(suggestionCriteria), pageable);
    }

    @Override
    public Visits updateVisitBySuggestion(SuggestionVisiteDTO suggestionVisiteDTO) {

        Visits v = visitsService.findVisitById(suggestionVisiteDTO.getIdVisit());
        Clients c = clientsService.getClientById(suggestionVisiteDTO.getIdClient());
        Optional<User> u = userService.getUserById(suggestionVisiteDTO.getIdUser());
        Optional<Suggestions> s = suggestionRepository.findSuggestionsById(suggestionVisiteDTO.getIdSuggestion());

        Suggestions suggestions = s.orElse(null);

        LocalDateTime dateTimeBefore = MethoUtils.convertorDateWithoutFormat(suggestionVisiteDTO.getDateVisiteText());

        if(suggestions != null){
            suggestions.setId(suggestionVisiteDTO.getIdSuggestion());
            suggestions.setValidated(Boolean.TRUE);

            suggestionRepository.save(suggestions);
        }

        User user = u.orElse(null);

        if (suggestionVisiteDTO.getParticipant() != null){

            List<VisitParticipants> visitParticipants = visitParticipantsService.getVisitParticipantsByIdVisit(suggestionVisiteDTO.getIdVisit());
            visitParticipantsService.deleteAll(visitParticipants);

            for (UserDTO userDTO: suggestionVisiteDTO.getParticipant()) {

                Optional<User> userById = userService.getUserById(Long.valueOf(userDTO.getId()));

                if (userById.isPresent()) {
                    VisitParticipants visitParticipant =  new VisitParticipants();
                    visitParticipant.setUser(userById.get());
                    visitParticipant.setVisit(v);
                    visitParticipantsService.save(visitParticipant);
                }
            }
        }

        User currentUser = userService.getCurrentUser();

        ConfigApplication configApplications = configApplicationService.findOne();

        saveNotificationRepository(currentUser, s.map(Suggestions::getUserAfbExpediteur).orElse(null), "VALIDATION DE LA SUGGESTION", patternNotification(configApplications != null ? configApplications.getPatternValidatedSuggestion(): "", suggestions, user));

        sendMail(Objects.requireNonNull(s.map(Suggestions::getUserAfbExpediteur).orElse(null)), "VALIDATION DE LA SUGGESTION", patternNotification(configApplications != null ? configApplications.getPatternValidatedSuggestion(): "", suggestions, user));

        if(v != null){
            v.setId(suggestionVisiteDTO.getIdVisit());
            v.setVisitDate(dateTimeBefore);
            if(suggestionVisiteDTO.getHourVisit() != null) {
                v.setHourVisit(suggestionVisiteDTO.getHourVisit());
            }
            v.setVisitObject(suggestionVisiteDTO.getVisitObject());
            v.setVisitType(suggestionVisiteDTO.getTypeVisit());
            v.setMoyenUtilise(suggestionVisiteDTO.getMoyenUtilise());
            if(suggestionVisiteDTO.getHourVisit() != null) {
                v.setHourVisit(suggestionVisiteDTO.getHourVisit());
            }
            v.setClient(c);
            v.setDateLastUpdate(LocalDateTime.now());
            v.setUserAfb(user);

            return visitsRepository.save(v);
        } else {
            return null;
        }
    }

    @Override
    public Suggestions notValidateSuggestionByVisit(SuggestionVisiteDTO suggestionVisiteDTO) {

        Optional<Suggestions> s = suggestionRepository.findSuggestionsById(suggestionVisiteDTO.getIdSuggestion());
        Optional<User> u = userService.getUserById(suggestionVisiteDTO.getIdUser());

        Suggestions suggestions = s.orElse(null);
        User user = u.orElse(null);

        User currentUser = userService.getCurrentUser();

        ConfigApplication configApplications = configApplicationService.findOne();

        saveNotificationRepository(currentUser, s.map(Suggestions::getUserAfbExpediteur).orElse(null), "SUGGESTION NON VALIDÉE", patternNotification(configApplications != null ? configApplications.getPatternNotValidatedSuggestion() : "", suggestions, user));

        if(suggestions != null) {
            suggestions.setId(suggestionVisiteDTO.getIdSuggestion());
            suggestions.setNotValidated(Boolean.TRUE);

            suggestions = suggestionRepository.save(suggestions);
        }
        sendMail(Objects.requireNonNull(s.map(Suggestions::getUserAfbExpediteur).orElse(null)), "SUGGESTION NON VALIDÉE", patternNotification(configApplications != null ? configApplications.getPatternNotValidatedSuggestion() : "", suggestions, user));

        return suggestions;
    }

    private void saveNotificationRepository(User currentUser, User user, String typeNotification, String description){
        Notifications notification = new Notifications();
        notification.setCreateBy(currentUser);
        notification.setTypeNotification(typeNotification);
        notification.setDescriptionNotification(description);
        notification.setCreateDate(LocalDateTime.now());
        notification.setStatus(Boolean.FALSE);
        notification.setSendTo(user);

        System.out.println("save notification " + notification);

        notificationRepository.save(notification);
    }

    private void sendMail(User user, String subject, String content) {
        if(user.getEmail() != null) {
            mailService.sendEmail(user.getEmail(), subject,
                    content, true, true);
        }
    }

    private static String patternNotification(String text, Suggestions suggestions, User user) {

        if(suggestions != null) {
            text = text
                    .replace("{NOM_CLIENT}", suggestions.getClients().getFirstName().toUpperCase()
                            + " " + suggestions.getClients().getLastName().toUpperCase());
            text = text.replace("{DATE_VISITE}", suggestions.getDateCreateText());
        }
        if(user != null) {
            text = text.replace("{NOM_EXPEDITEUR}", user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
        }
        return text;
    }
}
