package cm.skysoft.app.service.impl;

import cm.skysoft.app.criteria.AgencyUserCriteria;
import cm.skysoft.app.criteria.UserCriteriaDTO;
import cm.skysoft.app.criteria.VisitCriteriaDTO;
import cm.skysoft.app.domain.*;
import cm.skysoft.app.dto.*;
import cm.skysoft.app.repository.NotificationRepository;
import cm.skysoft.app.repository.VisitNoteRepository;
import cm.skysoft.app.repository.VisitsRepository;
import cm.skysoft.app.repository.specification.VisitSpecification;
import cm.skysoft.app.service.*;
import cm.skysoft.app.service.beans.DashboardVisitBeans;
import cm.skysoft.app.utils.MethoUtils;
import cm.skysoft.app.utils.SecurityUtils;
import io.undertow.util.BadRequestException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Created by francis on 2/12/21.
 */
@Service
public class VisitsServiceImpl implements VisitsService {

    private final Logger LOGGER = LoggerFactory.getLogger(VisitsServiceImpl.class);
    private String[] participants;
    private String userParticipants = "";

    private final EntityManager em;

    private final VisitsRepository visitsRepository;
    private final ClientsService clientsService;
    private final UserService userService ;
    private final AgencyUserService agencyUserService;
    private final NotificationsService notificationsService;
    private final NotificationRepository notificationRepository;
    private final VisitParticipantsService visitParticipantsService;
    private final ConfigApplicationService configApplicationService;
    private final AfbExternalService afbExternalService;
    private final MailService mailService;
    private final ConfigVisitService configVisitService;
    private final ConfigSmsService configSmsService;
    private final VisitNoteRepository visitNoteRepository;
    private final LogsService logsService;
    private final SendSMSService sendSMSService;


    public VisitsServiceImpl(EntityManager em, VisitsRepository visitsRepository, ClientsService clientsService,
                             UserService userService, AgencyUserService agencyUserService, NotificationsService notificationsService,
                             NotificationRepository notificationRepository, VisitParticipantsService visitParticipantsService,
                             ConfigApplicationService configApplicationService, AfbExternalService afbExternalService,
                             MailService mailService, ConfigVisitService configVisitService, ConfigSmsService configSmsService, VisitNoteRepository visitNoteRepository, LogsService logsService, SendSMSService sendSMSService) {
        this.em = em;
        this.visitsRepository = visitsRepository;
        this.clientsService = clientsService;
        this.userService = userService;
        this.agencyUserService = agencyUserService;
        this.notificationsService = notificationsService;
        this.notificationRepository = notificationRepository;
        this.visitParticipantsService = visitParticipantsService;
        this.configApplicationService = configApplicationService;
        this.afbExternalService = afbExternalService;
        this.mailService = mailService;
        this.configVisitService = configVisitService;
        this.configSmsService = configSmsService;
        this.visitNoteRepository = visitNoteRepository;
        this.logsService = logsService;
        this.sendSMSService = sendSMSService;
    }

    /**
     * save visitDTO.
     *
     * @param visitDTO the visitDTO.
     */
    @Transactional
    @Override
    public Visits save(VisitDTO visitDTO) {
        LOGGER.debug("REQ - save visitDTO "+ visitDTO);

        Visits v = new Visits();
        Clients c = new Clients();
        User user = userService.getCurrentUser();


        AgencyUser agencyUser = agencyUserService.getAgencyUserByIdUser(user.getId()).orElse(null);
        ClientDTO clientDTO = visitDTO.getClient();
        Clients client = null;

        if (clientDTO.getIdClient() != null) {
            client = clientsService.getClientByIdClient(clientDTO.getIdClient());
        } else if (clientDTO.getId() != null) {
            client = clientsService.getClientByIdClient(clientDTO.getId());
        }

        if (client != null){
            v.setClient(client);
        } else {

            c.setIdClient(clientDTO.getId());
            c.setCodeClient(clientDTO.getCodeClient());
            c.setFirstName(clientDTO.getFirstName());
            c.setLastName(clientDTO.getLastName());
            c.setLegalForm(clientDTO.getLegalForm());
            c.setActivityProfile(clientDTO.getActivityProfile());
            c.setParticularProfile(clientDTO.getParticularProfile());
            c.setEmail(clientDTO.getEmail());
            c.setBirthDate(clientDTO.getBirthDate());
            c.setBirthPlace(clientDTO.getBirthPlace());
            c.setEntryDate(clientDTO.getEntryDate());
            c.setManager(user);
            if(agencyUser != null) {
                c.setAgency(agencyUser.getAgency());
            }

            client = clientsService.save(c);
            v.setClient(client);
        }

        if (visitDTO.getId() != null) {
            v.setId(visitDTO.getId());
        }

        v.setVisitDate(visitDTO.getVisitDate());
        v.setVisitObject(visitDTO.getVisitObject());
        v.setVisitType(visitDTO.getVisitType());
        if(visitDTO.getHourVisit() != null) {
            v.setHourVisit(visitDTO.getHourVisit());
        }
        v.setMoyenUtilise(visitDTO.getMoyenUtilise());
        v.setUserAfb(user);
        v.setPlanification(false);
        v.setDateCreated(LocalDateTime.now());

        if (visitDTO.getLocalisation() != null && visitDTO.getMoyenUtilise().equals("Déscente sur le terrain")) {
            v.setLocalisation(visitDTO.getLocalisation());
        }

        v = visitsRepository.save(v);

        if (visitDTO.getParticipant() != null){

            for (UserDTO userDTO: visitDTO.getParticipant()) {

                Optional<User> u = userService.getUserById(Long.valueOf(userDTO.getId()));
                if (u.isPresent()) {
                    VisitParticipants visitParticipant =  new VisitParticipants();
                    visitParticipant.setUser(u.get());
                    visitParticipant.setVisit(v);
                    visitParticipantsService.save(visitParticipant);

                    userParticipants = visitParticipant.getUser().getFirstName().toUpperCase() +  " " + visitParticipant.getUser().getLastName().toUpperCase() + ", ";
                }
            }
        }

        logsService.save(new Logs(user, "Enregistrement d'une visite : "+ v.getVisitCode(), Logs.CREATION_VISITE));

        return v;
    }


    @Override
    public Visits saveVisitPrepa(VisitDTO visitDTO, String codeClient) {
        LOGGER.debug("REQ - save visitDTO "+ visitDTO);


        Visits v = new Visits();
        Clients c = new Clients();
        User user = userService.getCurrentUser();

        AgencyUser agencyUser = agencyUserService.getAgencyUserByIdUser(user.getId()).orElse(null);
        Clients clientDTO = clientsService.findClientsByCodeClient(codeClient);
        Clients client = null;

        if(clientDTO != null){
            if (clientDTO.getIdClient() != null && clientDTO.getId() == null) {
                client = clientsService.getClientByIdClient(clientDTO.getIdClient());
            } else if (clientDTO.getId() != null) {
                client = clientsService.getClientById(clientDTO.getId());
            }

            if (client != null){
                v.setClient(client);
            }
        }
        else {
            ClientDTO clientDTO1 = afbExternalService.getClientByClientCode(codeClient);

            c.setIdClient(clientDTO1.getIdClient());
            c.setCodeClient(clientDTO1.getCodeClient());
            c.setFirstName(clientDTO1.getFirstName());
            c.setLastName(clientDTO1.getLastName());
            c.setLegalForm(clientDTO1.getLegalForm());
            c.setActivityProfile(clientDTO1.getActivityProfile());
            c.setParticularProfile(clientDTO1.getParticularProfile());
            c.setEmail(clientDTO1.getEmail());
            c.setBirthDate(clientDTO1.getBirthDate());
            c.setBirthPlace(clientDTO1.getBirthPlace());
            c.setEntryDate(clientDTO1.getEntryDate());
            c.setManager(user);
            if (agencyUser != null) {
                c.setAgency(agencyUser.getAgency());
            }
            client = clientsService.save(c);
            v.setClient(client);
        }

        if (visitDTO.getId() != null) {
            v.setId(visitDTO.getId());
        }

        v.setVisitDate(visitDTO.getVisitDate());
        v.setVisitObject(visitDTO.getVisitObject());
        v.setVisitType(visitDTO.getVisitType());
        if(visitDTO.getHourVisit() != null) {
            v.setHourVisit(visitDTO.getHourVisit());
        }
        v.setMoyenUtilise(visitDTO.getMoyenUtilise());
        v.setUserAfb(user);
        v.setDateCreated(LocalDateTime.now());

        if (visitDTO.getLocalisation() != null && visitDTO.getMoyenUtilise().equals("Déscente sur le terrain")) {
            v.setLocalisation(visitDTO.getLocalisation());
        }
        v = visitsRepository.save(v);

        if (visitDTO.getParticipant() != null){

            for (UserDTO userDTO: visitDTO.getParticipant()) {

                Optional<User> u = userService.getUserById(Long.valueOf(userDTO.getId()));
                if (u.isPresent()) {
                    VisitParticipants visitParticipant =  new VisitParticipants();
                    visitParticipant.setUser(u.get());
                    visitParticipant.setVisit(v);
                    visitParticipantsService.save(visitParticipant);

                    userParticipants = visitParticipant.getUser().getFirstName().toUpperCase() + " " + visitParticipant.getUser().getLastName().toUpperCase() + ", ";
                }
            }
        }

        logsService.save(new Logs(user, "Preparation de la visite : "+ v.getVisitCode(), Logs.PREPARATION_VISITE));

        return v;
    }


    @Override
    public Visits saveVisitBureau(VisitDTO visitDTO, String codeClient) throws BadRequestException {
        LOGGER.debug("REQ - save visitDTO "+ visitDTO);

        Visits v = new Visits();
        Clients c = new Clients();
        User user = userService.getCurrentUser();
        ConfigVisit configVisit = configVisitService.getOne();
        VisitNote visitNote = new VisitNote();

        if(configVisit.getId() == null) {
            throw new BadRequestException();
        }

        AgencyUser agencyUser = agencyUserService.getAgencyUserByIdUser(user.getId()).orElse(null);
        Clients clientDTO = clientsService.findClientsByCodeClient(codeClient);
        Clients client = null;

        if(clientDTO != null){
            if (clientDTO.getIdClient() != null && clientDTO.getId() == null) {
                client = clientsService.getClientByIdClient(clientDTO.getIdClient());
            } else if (clientDTO.getId() != null) {
                client = clientsService.getClientById(clientDTO.getId());
            }

            if (client != null){
                v.setClient(client);
            }
        }
        else
        {
            ClientDTO clientDTO1 = afbExternalService.getClientByClientCode(codeClient);

            c.setIdClient(clientDTO1.getIdClient());
            c.setCodeClient(clientDTO1.getCodeClient());
            c.setFirstName(clientDTO1.getFirstName());
            c.setLastName(clientDTO1.getLastName());
            c.setLegalForm(clientDTO1.getLegalForm());
            c.setActivityProfile(clientDTO1.getActivityProfile());
            c.setParticularProfile(clientDTO1.getParticularProfile());
            c.setEmail(clientDTO1.getEmail());
            c.setBirthDate(clientDTO1.getBirthDate());
            c.setBirthPlace(clientDTO1.getBirthPlace());
            c.setEntryDate(clientDTO1.getEntryDate());
            c.setManager(user);
            if(agencyUser != null) {
                c.setAgency(agencyUser.getAgency());
            }
            client = clientsService.save(c);
            v.setClient(client);
        }

        if (visitDTO.getId() != null) {
            v.setId(visitDTO.getId());
        }

        Date date = new Date();
        Timestamp ts =new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        v.setVisitDate(LocalDateTime.now());
        v.setVisitObject(visitDTO.getVisitObject());
        v.setVisitType(configVisit.getTypeVisit());
        v.setMoyenUtilise(configVisit.getMoyenUtilise());
        v.setUserAfb(user);
        v.setDateCreated(LocalDateTime.now());
        v.setHourVisit(formatter.format(ts));
        if(agencyUser != null) {
            v.setLocalisation(agencyUser.getAgency().getName());
        }
        v.setPreparation(Boolean.TRUE);
        v.setPlanification(Boolean.TRUE);
        v.setExecution(Boolean.TRUE);
        v.setReporting(Boolean.FALSE);
        v.setArchivate(Boolean.FALSE);

        v = visitsRepository.save(v);

        if (visitDTO.getParticipant() != null) {

            for (UserDTO userDTO: visitDTO.getParticipant()) {

                Optional<User> u = userService.getUserById(Long.valueOf(userDTO.getId()));

                if (u.isPresent()) {
                    VisitParticipants visitParticipant =  new VisitParticipants();
                    visitParticipant.setUser(u.get());
                    visitParticipant.setVisit(v);
                    visitParticipantsService.save(visitParticipant);

                    userParticipants = visitParticipant.getUser().getFirstName().toUpperCase() +  " " + visitParticipant.getUser().getLastName().toUpperCase() + ", ";
                }
            }
        }

        visitNote.setNoteVisit(v.getVisitObject());
        visitNote.setVisits(v);
        visitNote.setDateCreated(LocalDateTime.now());
        visitNote.setUser(user);

        visitNoteRepository.save(visitNote);

        logsService.save(new Logs(user, "Engistrement de la visite au bureau : "+ v.getVisitCode(), Logs.CREATION_VISITE_BUREAU));

        return v;
    }


    @Transactional
    @Override
    public Visits validateVisit(Long idVisit) {

        if(idVisit != null) {

            ConfigSms configSms;
            ConfigApplication configApplications = null;
            StringBuilder participantPhoneNumber = new StringBuilder();

            User user = userService.getCurrentUser();
            Optional<Visits> visits = getVisitById(idVisit);
            if(visits.isPresent()) {
                List<VisitParticipants> visitParticipantsList = visitParticipantsService.getVisitParticipantsByIdVisit(visits.get().getId());

                visits.get().setPlanification(Boolean.TRUE);
                visits.get().setDateValidated(LocalDateTime.now());

                visitsRepository.save(visits.get());

                for (VisitParticipants visitParticipant: visitParticipantsList) {
                    if(userParticipants != null) {
                        userParticipants += visitParticipant.getUser().getFirstName().toUpperCase() + " " + visitParticipant.getUser().getLastName().toUpperCase() + ", ";
                    }
                    if(visitParticipant.getUser().getPhoneNumber() != null) {
                        participantPhoneNumber.append(visitParticipant.getUser().getPhoneNumber()).append(";");
                    }

                    configApplications = configApplicationService.findOne();
                    Notifications notification = new Notifications();

                    notification.setCreateBy(user);
                    notification.setCreateDate(LocalDateTime.now());
                    notification.setStatus(Boolean.FALSE);
                    notification.setTypeNotification("PLANIFICATION DE LA VISITE");
                    notification.setSendTo(visitParticipant.getUser());

                    if(userParticipants != null) {
                        notification.setDescriptionNotification(patternNotificationWithParticipant(configApplications != null ? configApplications.getPatternValidatedPlannedVisit() : "", visits.get(), user, userParticipants.substring(0, userParticipants.length() - 2)));
                    } else {
                        notification.setDescriptionNotification(patternNotification(configApplications != null ? configApplications.getPatternValidatedPlannedVisit() : "", visits.get(), user));
                    }

                    notificationRepository.save(notification);
                    logsService.save(new Logs(user, "Enregistrement notification lors de la validation e la visite: "
                            + notification.getDescriptionNotification(), Logs.ENREGISTREMENT_NOTIFICATION));
                }
            }

            visits.ifPresent(value -> logsService.save(new Logs(user, "Validation de la visite : " + value.getVisitCode(), Logs.VALIDATION_VISITE)));

            configSms = configSmsService.getOne();

            if (configSms != null && configApplications != null) {
                sendSMSService.send(configSms, patternNotificationWithParticipant(configApplications.getPatternValidatedPlannedVisit(), visits.get(), user, userParticipants), participantPhoneNumber.toString());
            }

            return visits.map(visitsRepository::save).orElse(null);
        } else {
            return null;
        }
    }

    /**
     * update visit.
     *
     * @param visitDTO the visit.
     */
    @Transactional
    @Override
    public Visits update(VisitDTO visitDTO) {
        LOGGER.debug("REQ - update visit");

        Visits v = visitsRepository.findVisitsById(visitDTO.getId());
        Clients c = new Clients();
        User user = userService.getCurrentUser();
        UserDTO userDTOs = new UserDTO();

        if(agencyUserService.getAgencyUserByIdUser(user.getId()).isPresent()) {
            AgencyUser agencyUser = agencyUserService.getAgencyUserByIdUser(user.getId()).orElse(null);

            ClientDTO clientDTO = visitDTO.getClient();
            Clients client = null;

            if (clientDTO.getIdClient() != null) {
                client = clientsService.getClientByIdClient(clientDTO.getIdClient());
            } else if (clientDTO.getId() != null) {
                client = clientsService.getClientByIdClient(clientDTO.getId());
            }

            if (client != null) {
                v.setClient(client);
            } else {

                c.setIdClient(clientDTO.getId());
                c.setCodeClient(clientDTO.getCodeClient());
                c.setFirstName(clientDTO.getFirstName());
                c.setLastName(clientDTO.getLastName());
                c.setLegalForm(clientDTO.getLegalForm());
                c.setActivityProfile(clientDTO.getActivityProfile());
                c.setParticularProfile(clientDTO.getParticularProfile());
                c.setEmail(clientDTO.getEmail());
                c.setBirthDate(clientDTO.getBirthDate());
                c.setBirthPlace(clientDTO.getBirthPlace());
                c.setEntryDate(clientDTO.getEntryDate());
                c.setManager(user);
                if(agencyUser != null) {
                    c.setAgency(agencyUser.getAgency());
                }

                client = clientsService.save(c);
                v.setClient(client);
            }

            v.setVisitDate(visitDTO.getVisitDate());
            v.setVisitObject(visitDTO.getVisitObject());
            v.setDateLastUpdate(LocalDateTime.now());
            if(visitDTO.getHourVisit() != null) {
                v.setHourVisit(visitDTO.getHourVisit());
            }
            v.setVisitType(visitDTO.getVisitType());
            v.setMoyenUtilise(visitDTO.getMoyenUtilise());

            if (visitDTO.getLocalisation() != null && visitDTO.getMoyenUtilise().equals("Déscente sur le terrain")) {
                v.setLocalisation(visitDTO.getLocalisation());
            }

            ConfigApplication configApplications = configApplicationService.findOne();
            NotificationDTO notificationDTO = new NotificationDTO();

            userDTOs.setId(String.valueOf(user.getId()));

            notificationDTO.setCreateBy(userDTOs);
            notificationDTO.setStatus(Boolean.FALSE);

            notificationDTO.setDescriptionNotification(patternNotification(configApplications != null ? configApplications.getPatternUpdatePlannedVisit() : "", v, user));

            notificationsService.save(notificationDTO);
            logsService.save(new Logs(user, "Enregistrement notification lors de la modification de la visite: "
                    + notificationDTO.getDescriptionNotification(), Logs.ENREGISTREMENT_NOTIFICATION));

            v = visitsRepository.save(v);

            if (visitDTO.getParticipant() != null) {

                List<VisitParticipants> visitParticipants = visitParticipantsService.getVisitParticipantsByIdVisit(visitDTO.getId());
                visitParticipantsService.deleteAll(visitParticipants);

                for (UserDTO userDTO : visitDTO.getParticipant()) {

                    Optional<User> u = userService.getUserById(Long.valueOf(userDTO.getId()));


                    if (u.isPresent()) {
                        VisitParticipants visitParticipant = new VisitParticipants();
                        visitParticipant.setUser(u.get());
                        visitParticipant.setVisit(v);
                        visitParticipantsService.save(visitParticipant);
                    }
                }
            }

            logsService.save(new Logs(user, "Modification de la visite au bureau : "+ v.getVisitCode(), Logs.CREATION_VISITE_BUREAU));

        }
        return v;
    }

    /**
     * get visit by id.
     *
     * @param id the id.
     * @return visit found.
     */
    @Override
    public Optional<Visits> getVisitById(Long id) {
        LOGGER.debug("REQ - get visit by id");
        return visitsRepository.findById(id);
    }

    @Override
    public Visits findVisitById(Long id) {
        return visitsRepository.findVisitsById(id);
    }

    /**
     * get all visit.
     *
     * @param pageable the pageable.
     * @return list of visit found.
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Visits> getAllVisits(Pageable pageable) {
        LOGGER.debug("REQ - get all visit page");
        Page<Visits> visits = visitsRepository.findAll(pageable);

        if(!visits.isEmpty()){
            return visits;
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    /**
     * get all visit.
     *
     * @return list of visit found.
     */
    @Transactional(readOnly = true)
    @Override
    public List<Visits> getAllVisits() {
        LOGGER.debug("REQ - get all visit");
        List<Visits> visits = visitsRepository.findAll();
        if (!visits.isEmpty()) {
            return visits;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * get list visit from participants
     * @return listVisit from participants
     */
    @Override
    public Page<Visits> getListVisitByParticipant(Long idClient, String typeVisit, LocalDateTime dateDebut,
                                                  LocalDateTime dateFin, Boolean status, Pageable pageable) {

        Query q = null;

        User user = userService.getCurrentUser();

        String query = " select v from Visits v, VisitParticipants vp where 1=1 ";

        if(idClient != null){
            query += " and v.client.id = :idClient ";
        }
        if(typeVisit != null){
            query += " and v.visitType = :typeVisit ";
        }
        if(dateDebut != null){
            query += " and v.visitDate >= :dateDebut ";
        }
        if(dateFin != null){
            query += " and v.visitDate <= :dateFin ";
        }
        if(dateDebut != null && dateFin != null){
            query += " and v.visitDate between :dateDebut and :dateFin ";
        }
        if(status != null){
            query += " and v.execution =:status ";
        }

        query += " and v.planification = TRUE ";

        query += " and v.id = vp.visit.id and vp.user.id = :user ";

        query += " order by v.id asc ";

        q = em.createQuery(query);

        q.setParameter("user", user.getId());

        if(idClient != null){
            q.setParameter("idClient", idClient);
        }
        if(typeVisit != null){
            q.setParameter("typeVisit", typeVisit);
        }
        if(dateDebut != null){
            q.setParameter("dateDebut", dateDebut);
        }
        if(dateFin != null){
            q.setParameter("dateFin", dateFin);
        }
        if(status != null){
            q.setParameter("status", status);
        }

        return new PageImpl<>(q.getResultList());
    }

    @Override
    public Long getNumberVisitToPreparate(User user) {
        return visitsRepository.getNumberVisiteToPreparate(user);
    }

    @Override
    public Long getNumberVisitToPlanned(User user) {
        return visitsRepository.getNumberVisiteToPlanned(user);
    }

    @Override
    public Long getNumberVisitCreatedToPreparate(User user) {
        return visitsRepository.getNumberVisiteCreatedToPreparate(user);
    }

    @Override
    public Long getNumberVisitForExecuted(User user) {
        return visitsRepository.getNumberVisiteForExecuted(user);
    }

    @Override
    public Long getNumberVisiteToExcecuted(User user) {
        return visitsRepository.getNumberVisiteToExcecuted(user);
    }

    @Override
    public Visits findVisitByCodeVisit(String codeVisit) {
        return visitsRepository.findVisitsByVisitCode(codeVisit);
    }

    @Override
    public Page<Visits> getAllVisit(VisitCriteriaDTO visitCriteria, Pageable pageable) {
        return visitsRepository.findAll(VisitSpecification.getSpecification(visitCriteria), pageable);
    }

    @Override
    public Page<Visits> getAllVisitPreparate(VisitCriteriaDTO visitCriteria, Pageable pageable) {

        User user = userService.getCurrentUser();

        List<UserAfbDTO> userAfbDTOList = afbExternalService.getAllUserSupervisedByUSerCode(user.getUserCode());
        Page<Visits> visitsList = getAllVisit(visitCriteria, pageable);

        List<Visits>  visitsList1 = visitsList.getContent();

        List<Visits> visits = new ArrayList<>();

        for (UserAfbDTO u: userAfbDTOList) {
            for (Visits v: visitsList1) {
                if (u.getUserCode().equals(v.getUserAfb().getUserCode())) {
                        visits.add(v);
                }
            }
        }

        return new PageImpl<>(visits);
    }

    @Override
    public void getBeetwenDateVisit() {
        List<Visits> visits = visitsRepository.findByExecution();
        LocalDateTime dateTime = LocalDateTime.now();
        Long value = null;

        for (Visits v : visits) {
            List<VisitParticipants> visitParticipants = visitParticipantsService.getVisitParticipantsByIdVisit(v.getId());
            value = ChronoUnit.DAYS.between(dateTime, v.getVisitDate());
            if(value == 6L) {
                sendMailVisit(value + 1L, v, visitParticipants);
                sendNotificationVisit(v, value + 1L, visitParticipants);
            } else if(value == 2L) {
                sendMailVisit(value + 1L, v, visitParticipants);
                sendNotificationVisit(v, value + 1L, visitParticipants);
            } else if(value == 0L) {
                if(dateTime.toString().substring(0, 10).equals(v.getVisitDate().toString().substring(0, 10))) {
                    sendMailVisitJJ(v, visitParticipants);
                    sendNotificationVisitJJ(v, visitParticipants);
                } else {
                    sendMailVisit(value + 1L, v, visitParticipants);
                    sendNotificationVisit(v, value + 1L, visitParticipants);
                }
            }
        }
    }

    @Override
    public void updatePreparationStatus(Long idVisit) {
        if(idVisit != null) {
            Optional<Visits> visits = getVisitById(idVisit);

            visits.ifPresent(value -> {
                value.setPreparation(Boolean.TRUE);
                visitsRepository.save(value);
            });
        }
    }

    @Override
    public DashboardVisitDTO getNumberTotalVisit(String dateBefore, String dateAfter, String periode) {
        LocalDate today = LocalDate.now();
        LocalDateTime todayTime = today.atStartOfDay();
        LocalDateTime dateBegin = null;

        LocalDateTime dateEnd = null;

        DashboardVisitDTO dashboardVisit = new DashboardVisitDTO();

        int totalVisit = 0;

        int totalPlannedVisitOustideTheOffice = 0;
        int totalPlannedVisitAtTheOffice = 0;
        int totalPlannedVisitPhoneCall = 0;

        int totalArchivateVisitOustideTheOffice = 0;
        int totalArchivateVisitAtTheOffice = 0;
        int totalArchivateVisitPhoneCall = 0;

        int totalVisitExecutedOustideTheOffice = 0;
        int totalVisitExecutedAtTheOffice = 0;
        int totalVisitExecutedPhoneCall = 0;

        int totalVisitNotExecutedOustideTheOffice = 0;
        int totalVisitNotExecutedAtTheOffice = 0;
        int totalVisitNotExecutedPhoneCall = 0;

        int totalVisitAwaitingReportOustideTheOffice = 0;
        int totalVisitAwaitingReportAtTheOffice = 0;
        int totalVisitAwaitingReportPhoneCall = 0;

        if (MethoUtils.WEEK.equals(periode)) {

            TemporalField field  = WeekFields.of(Locale.FRANCE).dayOfWeek();

            dateBegin = todayTime.with(field, 1);
            dateEnd = LocalDate.now().atTime(LocalTime.MAX).with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        } else if (MethoUtils.MONTH.equals(periode)) {

            dateBegin = todayTime.with(TemporalAdjusters.firstDayOfMonth());
            dateEnd = LocalDate.now().atTime(LocalTime.MAX).with(TemporalAdjusters.lastDayOfMonth());

        } else if (MethoUtils.QUARTERLY.equals(periode)) {

            dateBegin = today.minusMonths(3).atStartOfDay();
            dateEnd = LocalDate.now().atTime(LocalTime.MAX);

        } else if (MethoUtils.BIANNUAL.equals(periode)) {

            dateBegin = today.minusMonths(6).atStartOfDay();
            dateEnd = LocalDate.now().atTime(LocalTime.MAX);

        } else if (MethoUtils.YEAR.equals(periode)) {

            dateBegin = todayTime.with(TemporalAdjusters.firstDayOfYear());
            dateEnd = LocalDate.now().atTime(LocalTime.MAX).with(TemporalAdjusters.lastDayOfYear());

        }
        else {
            dateBegin = MethoUtils.INDEFINI_TEXT.equalsIgnoreCase(dateBefore) ? null
                    : MethoUtils.convertorDateTime(dateBefore);

            dateEnd = MethoUtils.INDEFINI_TEXT.equalsIgnoreCase(dateAfter) ? null
                    : MethoUtils.convertorDateTime(dateAfter);
        }

        if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
            Long numbertotalVisit = visitsRepository.getNumberTotalVisitAdmin(dateBegin, dateEnd);

            Long numbertotalVisitExecutedOustideTheOffice = visitsRepository.getNumberTotalVisitExecutedOutsideTheOfficheAdmin(dateBegin, dateEnd);
            Long numbertotalVisitExecutedAtTheOffice = visitsRepository.getNumberTotalVisitExecutedAtTheOfficheAdmin(dateBegin, dateEnd);
            Long numbertotalVisitExecutedPhoneCall = visitsRepository.getNumberTotalVisitExecutedPhoneCallAdmin(dateBegin, dateEnd);

            Long numberTotalPlannedVisitOutsideTheOffiche = visitsRepository.getNumberTotalPlannedVisitOutsideTheOfficheAdmin(dateBegin, dateEnd);
            Long numberTotalPlannedVisitAtTheOffice = visitsRepository.getNumberTotalPlannedVisitAtTheOfficheAdmin(dateBegin, dateEnd);
            Long numberTotalPlannedVisitPhoneCall = visitsRepository.getNumberTotalPlannedVisitPhoneCallAdmin(dateBegin, dateEnd);


            Long numberTotalArchivatedVisitOutsideTheOffiche = visitsRepository.getNumberTotalArchivatedVisitOutsideTheOfficheAdmin(dateBegin, dateEnd);
            Long numberTotalArchivatedVisitAtTheOffice = visitsRepository.getNumberTotalArchivatedVisitAtTheOfficheAdmin(dateBegin, dateEnd);
            Long numberTotalArchivatedVisitPhoneCall= visitsRepository.getNumberTotalArchivatedVisitPhoneCallAdmin(dateBegin, dateEnd);

            Long numberTotalVisitNotExecutedOutsideTheOffiche = visitsRepository.getNumberTotalVisitNotExecutedOutsideTheOfficheAdmin(dateBegin, dateEnd);
            Long numberTotalVisitNotExecutedAtTheOffice = visitsRepository.getNumberTotalVisitNotExecutedAtTheOfficheAdmin(dateBegin, dateEnd);
            Long numberTotalVisitNotExecutedPhoneCall= visitsRepository.getNumberTotalVisitNotExecutedPhoneCallAdmin(dateBegin, dateEnd);

            Long numberTotalVisitAwaitingReportOutsideTheOffiche = visitsRepository.getNumberTotalVisitAwaitingReportOutsideTheOfficheAdmin(dateBegin, dateEnd);
            Long numberTotalVisitAwaitingReportAtTheOffice = visitsRepository.getNumberTotalVisitAwaitingReportAtTheOfficheAdmin(dateBegin, dateEnd);
            Long numberTotalVisitAwaitingReportPhoneCall = visitsRepository.getNumberTotalVisitAwaitingReportPhoneCallAdmin(dateBegin, dateEnd);

            return getDashboardVisitDTO(dashboardVisit, totalVisit, totalPlannedVisitOustideTheOffice, totalPlannedVisitAtTheOffice, totalPlannedVisitPhoneCall, totalArchivateVisitOustideTheOffice, totalArchivateVisitAtTheOffice, totalArchivateVisitPhoneCall, totalVisitExecutedOustideTheOffice, totalVisitExecutedAtTheOffice, totalVisitExecutedPhoneCall, totalVisitNotExecutedOustideTheOffice, totalVisitNotExecutedAtTheOffice, totalVisitNotExecutedPhoneCall, totalVisitAwaitingReportOustideTheOffice, totalVisitAwaitingReportAtTheOffice, totalVisitAwaitingReportPhoneCall, numbertotalVisit, numbertotalVisitExecutedOustideTheOffice, numbertotalVisitExecutedAtTheOffice, numbertotalVisitExecutedPhoneCall, numberTotalPlannedVisitOutsideTheOffiche, numberTotalPlannedVisitAtTheOffice, numberTotalPlannedVisitPhoneCall, numberTotalArchivatedVisitOutsideTheOffiche, numberTotalArchivatedVisitAtTheOffice, numberTotalArchivatedVisitPhoneCall, numberTotalVisitNotExecutedOutsideTheOffiche, numberTotalVisitNotExecutedAtTheOffice, numberTotalVisitNotExecutedPhoneCall, numberTotalVisitAwaitingReportOutsideTheOffiche, numberTotalVisitAwaitingReportAtTheOffice, numberTotalVisitAwaitingReportPhoneCall);
        } else {

            Long numbertotalVisit = visitsRepository.getNumberTotalVisit(userService.getCurrentUser(), dateBegin, dateEnd);

            Long numbertotalVisitExecutedOustideTheOffice = visitsRepository.getNumberTotalVisitExecutedOutsideTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numbertotalVisitExecutedAtTheOffice = visitsRepository.getNumberTotalVisitExecutedAtTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numbertotalVisitExecutedPhoneCall = visitsRepository.getNumberTotalVisitExecutedPhoneCall(userService.getCurrentUser(), dateBegin, dateEnd);

            Long numberTotalPlannedVisitOutsideTheOffiche = visitsRepository.getNumberTotalPlannedVisitOutsideTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numberTotalPlannedVisitAtTheOffice = visitsRepository.getNumberTotalPlannedVisitAtTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numberTotalPlannedVisitPhoneCall = visitsRepository.getNumberTotalPlannedVisitPhoneCall(userService.getCurrentUser(), dateBegin, dateEnd);


            Long numberTotalArchivateVisitOutsideTheOffiche = visitsRepository.getNumberTotalArchivateVisitOutsideTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numberTotalArchivateVisitAtTheOffice = visitsRepository.getNumberTotalArchivateVisitAtTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numberTotalArchivateVisitPhoneCall = visitsRepository.getNumberTotalArchivateVisitPhoneCall(userService.getCurrentUser(), dateBegin, dateEnd);

            Long numberTotalVisitNotExecutedOutsideTheOffiche = visitsRepository.getNumberTotalVisitNotExecutedOutsideTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numberTotalVisitNotExecutedAtTheOffice = visitsRepository.getNumberTotalVisitNotExecutedAtTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numberTotalVisitNotExecutedPhoneCall = visitsRepository.getNumberTotalVisitNotExecutedPhoneCall(userService.getCurrentUser(), dateBegin, dateEnd);

            Long numberTotalVisitAwaitingReportOutsideTheOffiche = visitsRepository.getNumberTotalVisitAwaitingReportOutsideTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numberTotalVisitAwaitingReportAtTheOffice = visitsRepository.getNumberTotalVisitAwaitingReportAtTheOffiche(userService.getCurrentUser(), dateBegin, dateEnd);
            Long numberTotalVisitAwaitingReportPhoneCall = visitsRepository.getNumberTotalVisitAwaitingReportPhoneCall(userService.getCurrentUser(), dateBegin, dateEnd);

            return getDashboardVisitDTO(dashboardVisit, totalVisit, totalPlannedVisitOustideTheOffice, totalPlannedVisitAtTheOffice, totalPlannedVisitPhoneCall, totalArchivateVisitOustideTheOffice, totalArchivateVisitAtTheOffice, totalArchivateVisitPhoneCall, totalVisitExecutedOustideTheOffice, totalVisitExecutedAtTheOffice, totalVisitExecutedPhoneCall, totalVisitNotExecutedOustideTheOffice, totalVisitNotExecutedAtTheOffice, totalVisitNotExecutedPhoneCall, totalVisitAwaitingReportOustideTheOffice, totalVisitAwaitingReportAtTheOffice, totalVisitAwaitingReportPhoneCall, numbertotalVisit, numbertotalVisitExecutedOustideTheOffice, numbertotalVisitExecutedAtTheOffice, numbertotalVisitExecutedPhoneCall, numberTotalPlannedVisitOutsideTheOffiche, numberTotalPlannedVisitAtTheOffice, numberTotalPlannedVisitPhoneCall, numberTotalArchivateVisitOutsideTheOffiche, numberTotalArchivateVisitAtTheOffice, numberTotalArchivateVisitPhoneCall, numberTotalVisitNotExecutedOutsideTheOffiche, numberTotalVisitNotExecutedAtTheOffice, numberTotalVisitNotExecutedPhoneCall, numberTotalVisitAwaitingReportOutsideTheOffiche, numberTotalVisitAwaitingReportAtTheOffice, numberTotalVisitAwaitingReportPhoneCall);
        }
    }

    @NotNull
    private DashboardVisitDTO getDashboardVisitDTO(DashboardVisitDTO dashboardVisit, int totalVisit, int totalPlannedVisitOustideTheOffice, int totalPlannedVisitAtTheOffice, int totalPlannedVisitPhoneCall, int totalArchivateVisitOustideTheOffice, int totalArchivateVisitAtTheOffice, int totalArchivateVisitPhoneCall, int totalVisitExecutedOustideTheOffice, int totalVisitExecutedAtTheOffice, int totalVisitExecutedPhoneCall, int totalVisitNotExecutedOustideTheOffice, int totalVisitNotExecutedAtTheOffice, int totalVisitNotExecutedPhoneCall, int totalVisitAwaitingReportOustideTheOffice, int totalVisitAwaitingReportAtTheOffice, int totalVisitAwaitingReportPhoneCall, Long numbertotalVisit, Long numbertotalVisitExecutedOustideTheOffice, Long numbertotalVisitExecutedAtTheOffice, Long numbertotalVisitExecutedPhoneCall, Long numberTotalPlannedVisitOutsideTheOffiche, Long numberTotalPlannedVisitAtTheOffice, Long numberTotalPlannedVisitPhoneCall, Long numberTotalArchivateVisitOutsideTheOffiche, Long numberTotalArchivateVisitAtTheOffice, Long numberTotalArchivateVisitPhoneCall, Long numberTotalVisitNotExecutedOutsideTheOffiche, Long numberTotalVisitNotExecutedAtTheOffice, Long numberTotalVisitNotExecutedPhoneCall, Long numberTotalVisitAwaitingReportOutsideTheOffiche, Long numberTotalVisitAwaitingReportAtTheOffice, Long numberTotalVisitAwaitingReportPhoneCall) {
        numbertotalVisit = numbertotalVisit == null ? Long.valueOf(0) : numbertotalVisit;

        numbertotalVisitExecutedOustideTheOffice = numbertotalVisitExecutedOustideTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedOustideTheOffice;
        numbertotalVisitExecutedAtTheOffice = numbertotalVisitExecutedAtTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedAtTheOffice;
        numbertotalVisitExecutedPhoneCall = numbertotalVisitExecutedPhoneCall == null ? Long.valueOf(0) : numbertotalVisitExecutedPhoneCall;

        numberTotalPlannedVisitOutsideTheOffiche = numberTotalPlannedVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalPlannedVisitOutsideTheOffiche;
        numberTotalPlannedVisitAtTheOffice = numberTotalPlannedVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalPlannedVisitAtTheOffice;
        numberTotalPlannedVisitPhoneCall = numberTotalPlannedVisitPhoneCall == null ? Long.valueOf(0) : numberTotalPlannedVisitPhoneCall;

        numberTotalArchivateVisitOutsideTheOffiche = numberTotalArchivateVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalArchivateVisitOutsideTheOffiche;
        numberTotalArchivateVisitAtTheOffice = numberTotalArchivateVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalArchivateVisitAtTheOffice;
        numberTotalArchivateVisitPhoneCall = numberTotalArchivateVisitPhoneCall == null ? Long.valueOf(0) : numberTotalArchivateVisitPhoneCall;

        numberTotalVisitNotExecutedOutsideTheOffiche = numberTotalVisitNotExecutedOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitNotExecutedOutsideTheOffiche;
        numberTotalVisitNotExecutedAtTheOffice = numberTotalVisitNotExecutedAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitNotExecutedAtTheOffice;
        numberTotalVisitNotExecutedPhoneCall = numberTotalVisitNotExecutedPhoneCall == null ? Long.valueOf(0) : numberTotalVisitNotExecutedPhoneCall;

        numberTotalVisitAwaitingReportOutsideTheOffiche = numberTotalVisitAwaitingReportOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportOutsideTheOffiche;
        numberTotalVisitAwaitingReportAtTheOffice = numberTotalVisitAwaitingReportAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportAtTheOffice;
        numberTotalVisitAwaitingReportPhoneCall = numberTotalVisitAwaitingReportPhoneCall == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportPhoneCall;


        totalVisit += numbertotalVisit.intValue();

        totalPlannedVisitOustideTheOffice += numberTotalPlannedVisitOutsideTheOffiche.intValue();
        totalPlannedVisitAtTheOffice += numberTotalPlannedVisitAtTheOffice.intValue();
        totalPlannedVisitPhoneCall += numberTotalPlannedVisitPhoneCall.intValue();

        totalArchivateVisitOustideTheOffice += numberTotalArchivateVisitOutsideTheOffiche.intValue();
        totalArchivateVisitAtTheOffice += numberTotalArchivateVisitAtTheOffice.intValue();
        totalArchivateVisitPhoneCall += numberTotalArchivateVisitPhoneCall.intValue();

        totalVisitNotExecutedOustideTheOffice += numberTotalVisitNotExecutedOutsideTheOffiche.intValue();
        totalVisitNotExecutedAtTheOffice += numberTotalVisitNotExecutedAtTheOffice.intValue();
        totalVisitNotExecutedPhoneCall += numberTotalVisitNotExecutedPhoneCall.intValue();

        totalVisitExecutedOustideTheOffice += numbertotalVisitExecutedOustideTheOffice.intValue();
        totalVisitExecutedAtTheOffice += numbertotalVisitExecutedAtTheOffice.intValue();
        totalVisitExecutedPhoneCall += numbertotalVisitExecutedPhoneCall.intValue();

        totalVisitAwaitingReportOustideTheOffice += numberTotalVisitAwaitingReportOutsideTheOffiche.intValue();
        totalVisitAwaitingReportAtTheOffice += numberTotalVisitAwaitingReportAtTheOffice.intValue();
        totalVisitAwaitingReportPhoneCall += numberTotalVisitAwaitingReportPhoneCall.intValue();


        dashboardVisit.setTotalVisit(totalVisit);

        dashboardVisit.setTotalPlannedVisitAtTheOffice(totalPlannedVisitAtTheOffice);
        dashboardVisit.setTotalPlannedVisitOustideTheOffice(totalPlannedVisitOustideTheOffice);
        dashboardVisit.setTotalPlannedVisitPhoneCall(totalPlannedVisitPhoneCall);

        dashboardVisit.setTotalArchivateVisitAtTheOffice(totalArchivateVisitAtTheOffice);
        dashboardVisit.setTotalArchivateVisitOustideTheOffice(totalArchivateVisitOustideTheOffice);
        dashboardVisit.setTotalArchivateVisitPhoneCall(totalArchivateVisitPhoneCall);

        dashboardVisit.setTotalVisitExecutedAtTheOffice(totalVisitExecutedAtTheOffice);
        dashboardVisit.setTotalVisitExecutedOustideTheOffice(totalVisitExecutedOustideTheOffice);
        dashboardVisit.setTotalVisitExecutedPhoneCall(totalVisitExecutedPhoneCall);

        dashboardVisit.setTotalVisitNotExecutedAtTheOffice(totalVisitNotExecutedAtTheOffice);
        dashboardVisit.setTotalVisitNotExecutedOustideTheOffice(totalVisitNotExecutedOustideTheOffice);
        dashboardVisit.setTotalVisitNotExecutedPhoneCall(totalVisitNotExecutedPhoneCall);

        dashboardVisit.setTotalVisitAwaitingReportAtTheOffice(totalVisitAwaitingReportAtTheOffice);
        dashboardVisit.setTotalVisitAwaitingReportOustideTheOffice(totalVisitAwaitingReportOustideTheOffice);
        dashboardVisit.setTotalVisitAwaitingReportPhoneCall(totalVisitAwaitingReportPhoneCall);

        return dashboardVisit;
    }

    @Override
    public List<DashboardVisitBeans> getNumberTotalVisitForUserSupervised(String dateBefore, String dateAfter, String periode, AgencyUserCriteria agencyUserCriteria) {

        LocalDate today = LocalDate.now();
        LocalDateTime todayTime = today.atStartOfDay();
        LocalDateTime dateBegin = null;

        LocalDateTime dateEnd = null;

        DashboardVisitBeans dashboardVisit = null;

        int totalVisit;

        int totalPlannedVisitOustideTheOffice;
        int totalPlannedVisitAtTheOffice;
        int totalPlannedVisitPhoneCall;

        int totalArchivateVisitOustideTheOffice;
        int totalArchivateVisitAtTheOffice;
        int totalArchivateVisitPhoneCall;

        int totalVisitExecutedOustideTheOffice;
        int totalVisitExecutedAtTheOffice;
        int totalVisitExecutedPhoneCall;

        int totalVisitNotExecutedOustideTheOffice;
        int totalVisitNotExecutedAtTheOffice;
        int totalVisitNotExecutedPhoneCall;

        int totalVisitAwaitingReportOustideTheOffice;
        int totalVisitAwaitingReportAtTheOffice;
        int totalVisitAwaitingReportPhoneCall;

        if (MethoUtils.WEEK.equals(periode)) {

            TemporalField field  = WeekFields.of(Locale.FRANCE).dayOfWeek();

            dateBegin = todayTime.with(field, 1);
            dateEnd = LocalDate.now().atTime(LocalTime.MAX).with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        } else if (MethoUtils.MONTH.equals(periode)) {

            dateBegin = todayTime.with(TemporalAdjusters.firstDayOfMonth());
            dateEnd = LocalDate.now().atTime(LocalTime.MAX).with(TemporalAdjusters.lastDayOfMonth());

        } else if (MethoUtils.QUARTERLY.equals(periode)) {

            dateBegin = today.minusMonths(3).atStartOfDay();
            dateEnd = LocalDate.now().atTime(LocalTime.MAX);

        } else if (MethoUtils.BIANNUAL.equals(periode)) {

            dateBegin = today.minusMonths(6).atStartOfDay();
            dateEnd = LocalDate.now().atTime(LocalTime.MAX);

        } else if (MethoUtils.YEAR.equals(periode)) {

            dateBegin = todayTime.with(TemporalAdjusters.firstDayOfYear());
            dateEnd = LocalDate.now().atTime(LocalTime.MAX).with(TemporalAdjusters.lastDayOfYear());

        }
        else {
            dateBegin = MethoUtils.INDEFINI_TEXT.equalsIgnoreCase(dateBefore) ? null
                    : MethoUtils.convertorDateTime(dateBefore);

            dateEnd = MethoUtils.INDEFINI_TEXT.equalsIgnoreCase(dateAfter) ? null
                    : MethoUtils.convertorDateTime(dateAfter);
        }

        User user = userService.getCurrentUser();
        List<DashboardVisitBeans> dashboardVisitBeansList = new ArrayList<>();
        List<User> userList = userService.getAllUsersListForAdmin(agencyUserCriteria);

        if(!SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) {

            List<UserAfbDTO> userAfbDTOList = afbExternalService.getAllUserSupervisedByUSerCode(user.getUserCode());

            if (!userAfbDTOList.isEmpty()) {
                if (userList.size() > userAfbDTOList.size()) {
                    for (User userDTO : userList) {
                        for (UserAfbDTO userAfbDTO : userAfbDTOList) {
                            if (userDTO.getUserCode().equals(userAfbDTO.getUserCode())) {

                                dashboardVisit = new DashboardVisitBeans();

                                totalVisit = 0;

                                totalPlannedVisitOustideTheOffice = 0;
                                totalPlannedVisitAtTheOffice = 0;
                                totalPlannedVisitPhoneCall = 0;

                                totalArchivateVisitOustideTheOffice = 0;
                                totalArchivateVisitAtTheOffice = 0;
                                totalArchivateVisitPhoneCall = 0;

                                totalVisitExecutedOustideTheOffice = 0;
                                totalVisitExecutedAtTheOffice = 0;
                                totalVisitExecutedPhoneCall = 0;

                                totalVisitNotExecutedOustideTheOffice = 0;
                                totalVisitNotExecutedAtTheOffice = 0;
                                totalVisitNotExecutedPhoneCall = 0;

                                totalVisitAwaitingReportOustideTheOffice = 0;
                                totalVisitAwaitingReportAtTheOffice = 0;
                                totalVisitAwaitingReportPhoneCall = 0;

                                Long numbertotalVisit = visitsRepository.getNumberTotalVisit(userDTO, dateBegin, dateEnd);

                                Long numbertotalVisitExecutedOustideTheOffice = visitsRepository.getNumberTotalVisitExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numbertotalVisitExecutedAtTheOffice = visitsRepository.getNumberTotalVisitExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numbertotalVisitExecutedPhoneCall = visitsRepository.getNumberTotalVisitExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                                Long numberTotalPlannedVisitOutsideTheOffiche = visitsRepository.getNumberTotalPlannedVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalPlannedVisitAtTheOffice = visitsRepository.getNumberTotalPlannedVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalPlannedVisitPhoneCall = visitsRepository.getNumberTotalPlannedVisitPhoneCall(userDTO, dateBegin, dateEnd);


                                Long numberTotalArchivateVisitOutsideTheOffiche = visitsRepository.getNumberTotalArchivateVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalArchivateVisitAtTheOffice = visitsRepository.getNumberTotalArchivateVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalArchivateVisitPhoneCall = visitsRepository.getNumberTotalArchivateVisitPhoneCall(userDTO, dateBegin, dateEnd);

                                Long numberTotalVisitNotExecutedOutsideTheOffiche = visitsRepository.getNumberTotalVisitNotExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalVisitNotExecutedAtTheOffice = visitsRepository.getNumberTotalVisitNotExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalVisitNotExecutedPhoneCall = visitsRepository.getNumberTotalVisitNotExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                                Long numberTotalVisitAwaitingReportOutsideTheOffiche = visitsRepository.getNumberTotalVisitAwaitingReportOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalVisitAwaitingReportAtTheOffice = visitsRepository.getNumberTotalVisitAwaitingReportAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalVisitAwaitingReportPhoneCall = visitsRepository.getNumberTotalVisitAwaitingReportPhoneCall(userDTO, dateBegin, dateEnd);


                                numbertotalVisit = numbertotalVisit == null ? Long.valueOf(0) : numbertotalVisit;

                                numbertotalVisitExecutedOustideTheOffice = numbertotalVisitExecutedOustideTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedOustideTheOffice;
                                numbertotalVisitExecutedAtTheOffice = numbertotalVisitExecutedAtTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedAtTheOffice;
                                numbertotalVisitExecutedPhoneCall = numbertotalVisitExecutedPhoneCall == null ? Long.valueOf(0) : numbertotalVisitExecutedPhoneCall;

                                numberTotalPlannedVisitOutsideTheOffiche = numberTotalPlannedVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalPlannedVisitOutsideTheOffiche;
                                numberTotalPlannedVisitAtTheOffice = numberTotalPlannedVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalPlannedVisitAtTheOffice;
                                numberTotalPlannedVisitPhoneCall = numberTotalPlannedVisitPhoneCall == null ? Long.valueOf(0) : numberTotalPlannedVisitPhoneCall;

                                numberTotalArchivateVisitOutsideTheOffiche = numberTotalArchivateVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalArchivateVisitOutsideTheOffiche;
                                numberTotalArchivateVisitAtTheOffice = numberTotalArchivateVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalArchivateVisitAtTheOffice;
                                numberTotalArchivateVisitPhoneCall = numberTotalArchivateVisitPhoneCall == null ? Long.valueOf(0) : numberTotalArchivateVisitPhoneCall;

                                numberTotalVisitNotExecutedOutsideTheOffiche = numberTotalVisitNotExecutedOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitNotExecutedOutsideTheOffiche;
                                numberTotalVisitNotExecutedAtTheOffice = numberTotalVisitNotExecutedAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitNotExecutedAtTheOffice;
                                numberTotalVisitNotExecutedPhoneCall = numberTotalVisitNotExecutedPhoneCall == null ? Long.valueOf(0) : numberTotalVisitNotExecutedPhoneCall;

                                numberTotalVisitAwaitingReportOutsideTheOffiche = numberTotalVisitAwaitingReportOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportOutsideTheOffiche;
                                numberTotalVisitAwaitingReportAtTheOffice = numberTotalVisitAwaitingReportAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportAtTheOffice;
                                numberTotalVisitAwaitingReportPhoneCall = numberTotalVisitAwaitingReportPhoneCall == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportPhoneCall;


                                totalVisit += numbertotalVisit.intValue();

                                totalPlannedVisitOustideTheOffice += numberTotalPlannedVisitOutsideTheOffiche.intValue();
                                totalPlannedVisitAtTheOffice += numberTotalPlannedVisitAtTheOffice.intValue();
                                totalPlannedVisitPhoneCall += numberTotalPlannedVisitPhoneCall.intValue();

                                totalArchivateVisitOustideTheOffice += numberTotalArchivateVisitOutsideTheOffiche.intValue();
                                totalArchivateVisitAtTheOffice += numberTotalArchivateVisitAtTheOffice.intValue();
                                totalArchivateVisitPhoneCall += numberTotalArchivateVisitPhoneCall.intValue();

                                totalVisitNotExecutedOustideTheOffice += numberTotalVisitNotExecutedOutsideTheOffiche.intValue();
                                totalVisitNotExecutedAtTheOffice += numberTotalVisitNotExecutedAtTheOffice.intValue();
                                totalVisitNotExecutedPhoneCall += numberTotalVisitNotExecutedPhoneCall.intValue();

                                totalVisitExecutedOustideTheOffice += numbertotalVisitExecutedOustideTheOffice.intValue();
                                totalVisitExecutedAtTheOffice += numbertotalVisitExecutedAtTheOffice.intValue();
                                totalVisitExecutedPhoneCall += numbertotalVisitExecutedPhoneCall.intValue();

                                totalVisitAwaitingReportOustideTheOffice += numberTotalVisitAwaitingReportOutsideTheOffiche.intValue();
                                totalVisitAwaitingReportAtTheOffice += numberTotalVisitAwaitingReportAtTheOffice.intValue();
                                totalVisitAwaitingReportPhoneCall += numberTotalVisitAwaitingReportPhoneCall.intValue();


                                dashboardVisit.setTotalVisit(totalVisit);

                                dashboardVisit.setUser(userDTO);
                                dashboardVisit.setTotalPlannedVisitAtTheOffice(totalPlannedVisitAtTheOffice);
                                dashboardVisit.setTotalPlannedVisitOustideTheOffice(totalPlannedVisitOustideTheOffice);
                                dashboardVisit.setTotalPlannedVisitPhoneCall(totalPlannedVisitPhoneCall);

                                dashboardVisit.setTotalArchivateVisitAtTheOffice(totalArchivateVisitAtTheOffice);
                                dashboardVisit.setTotalArchivateVisitOustideTheOffice(totalArchivateVisitOustideTheOffice);
                                dashboardVisit.setTotalArchivateVisitPhoneCall(totalArchivateVisitPhoneCall);

                                dashboardVisit.setTotalVisitExecutedAtTheOffice(totalVisitExecutedAtTheOffice);
                                dashboardVisit.setTotalVisitExecutedOustideTheOffice(totalVisitExecutedOustideTheOffice);
                                dashboardVisit.setTotalVisitExecutedPhoneCall(totalVisitExecutedPhoneCall);

                                dashboardVisit.setTotalVisitNotExecutedAtTheOffice(totalVisitNotExecutedAtTheOffice);
                                dashboardVisit.setTotalVisitNotExecutedOustideTheOffice(totalVisitNotExecutedOustideTheOffice);
                                dashboardVisit.setTotalVisitNotExecutedPhoneCall(totalVisitNotExecutedPhoneCall);

                                dashboardVisit.setTotalVisitAwaitingReportAtTheOffice(totalVisitAwaitingReportAtTheOffice);
                                dashboardVisit.setTotalVisitAwaitingReportOustideTheOffice(totalVisitAwaitingReportOustideTheOffice);
                                dashboardVisit.setTotalVisitAwaitingReportPhoneCall(totalVisitAwaitingReportPhoneCall);

                                dashboardVisitBeansList.add(dashboardVisit);
                            }
                        }
                    }
                } else {
                    for (UserAfbDTO userAfbDTO : userAfbDTOList) {
                        for (User userDTO : userList) {
                            if (userDTO.getUserCode().equals(userAfbDTO.getUserCode())) {

                                dashboardVisit = new DashboardVisitBeans();

                                totalVisit = 0;

                                totalPlannedVisitOustideTheOffice = 0;
                                totalPlannedVisitAtTheOffice = 0;
                                totalPlannedVisitPhoneCall = 0;

                                totalArchivateVisitOustideTheOffice = 0;
                                totalArchivateVisitAtTheOffice = 0;
                                totalArchivateVisitPhoneCall = 0;

                                totalVisitExecutedOustideTheOffice = 0;
                                totalVisitExecutedAtTheOffice = 0;
                                totalVisitExecutedPhoneCall = 0;

                                totalVisitNotExecutedOustideTheOffice = 0;
                                totalVisitNotExecutedAtTheOffice = 0;
                                totalVisitNotExecutedPhoneCall = 0;

                                totalVisitAwaitingReportOustideTheOffice = 0;
                                totalVisitAwaitingReportAtTheOffice = 0;
                                totalVisitAwaitingReportPhoneCall = 0;

                                Long numbertotalVisit = visitsRepository.getNumberTotalVisit(userDTO, dateBegin, dateEnd);

                                Long numbertotalVisitExecutedOustideTheOffice = visitsRepository.getNumberTotalVisitExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numbertotalVisitExecutedAtTheOffice = visitsRepository.getNumberTotalVisitExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numbertotalVisitExecutedPhoneCall = visitsRepository.getNumberTotalVisitExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                                Long numberTotalPlannedVisitOutsideTheOffiche = visitsRepository.getNumberTotalPlannedVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalPlannedVisitAtTheOffice = visitsRepository.getNumberTotalPlannedVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalPlannedVisitPhoneCall = visitsRepository.getNumberTotalPlannedVisitPhoneCall(userDTO, dateBegin, dateEnd);


                                Long numberTotalArchivateVisitOutsideTheOffiche = visitsRepository.getNumberTotalArchivateVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalArchivateVisitAtTheOffice = visitsRepository.getNumberTotalArchivateVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalArchivateVisitPhoneCall = visitsRepository.getNumberTotalArchivateVisitPhoneCall(userDTO, dateBegin, dateEnd);

                                Long numberTotalVisitNotExecutedOutsideTheOffiche = visitsRepository.getNumberTotalVisitNotExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalVisitNotExecutedAtTheOffice = visitsRepository.getNumberTotalVisitNotExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalVisitNotExecutedPhoneCall = visitsRepository.getNumberTotalVisitNotExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                                Long numberTotalVisitAwaitingReportOutsideTheOffiche = visitsRepository.getNumberTotalVisitAwaitingReportOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalVisitAwaitingReportAtTheOffice = visitsRepository.getNumberTotalVisitAwaitingReportAtTheOffiche(userDTO, dateBegin, dateEnd);
                                Long numberTotalVisitAwaitingReportPhoneCall = visitsRepository.getNumberTotalVisitAwaitingReportPhoneCall(userDTO, dateBegin, dateEnd);


                                numbertotalVisit = numbertotalVisit == null ? Long.valueOf(0) : numbertotalVisit;

                                numbertotalVisitExecutedOustideTheOffice = numbertotalVisitExecutedOustideTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedOustideTheOffice;
                                numbertotalVisitExecutedAtTheOffice = numbertotalVisitExecutedAtTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedAtTheOffice;
                                numbertotalVisitExecutedPhoneCall = numbertotalVisitExecutedPhoneCall == null ? Long.valueOf(0) : numbertotalVisitExecutedPhoneCall;

                                numberTotalPlannedVisitOutsideTheOffiche = numberTotalPlannedVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalPlannedVisitOutsideTheOffiche;
                                numberTotalPlannedVisitAtTheOffice = numberTotalPlannedVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalPlannedVisitAtTheOffice;
                                numberTotalPlannedVisitPhoneCall = numberTotalPlannedVisitPhoneCall == null ? Long.valueOf(0) : numberTotalPlannedVisitPhoneCall;

                                numberTotalArchivateVisitOutsideTheOffiche = numberTotalArchivateVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalArchivateVisitOutsideTheOffiche;
                                numberTotalArchivateVisitAtTheOffice = numberTotalArchivateVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalArchivateVisitAtTheOffice;
                                numberTotalArchivateVisitPhoneCall = numberTotalArchivateVisitPhoneCall == null ? Long.valueOf(0) : numberTotalArchivateVisitPhoneCall;

                                numberTotalVisitNotExecutedOutsideTheOffiche = numberTotalVisitNotExecutedOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitNotExecutedOutsideTheOffiche;
                                numberTotalVisitNotExecutedAtTheOffice = numberTotalVisitNotExecutedAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitNotExecutedAtTheOffice;
                                numberTotalVisitNotExecutedPhoneCall = numberTotalVisitNotExecutedPhoneCall == null ? Long.valueOf(0) : numberTotalVisitNotExecutedPhoneCall;

                                numberTotalVisitAwaitingReportOutsideTheOffiche = numberTotalVisitAwaitingReportOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportOutsideTheOffiche;
                                numberTotalVisitAwaitingReportAtTheOffice = numberTotalVisitAwaitingReportAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportAtTheOffice;
                                numberTotalVisitAwaitingReportPhoneCall = numberTotalVisitAwaitingReportPhoneCall == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportPhoneCall;


                                totalVisit += numbertotalVisit.intValue();

                                totalPlannedVisitOustideTheOffice += numberTotalPlannedVisitOutsideTheOffiche.intValue();
                                totalPlannedVisitAtTheOffice += numberTotalPlannedVisitAtTheOffice.intValue();
                                totalPlannedVisitPhoneCall += numberTotalPlannedVisitPhoneCall.intValue();

                                totalArchivateVisitOustideTheOffice += numberTotalArchivateVisitOutsideTheOffiche.intValue();
                                totalArchivateVisitAtTheOffice += numberTotalArchivateVisitAtTheOffice.intValue();
                                totalArchivateVisitPhoneCall += numberTotalArchivateVisitPhoneCall.intValue();

                                totalVisitNotExecutedOustideTheOffice += numberTotalVisitNotExecutedOutsideTheOffiche.intValue();
                                totalVisitNotExecutedAtTheOffice += numberTotalVisitNotExecutedAtTheOffice.intValue();
                                totalVisitNotExecutedPhoneCall += numberTotalVisitNotExecutedPhoneCall.intValue();

                                totalVisitExecutedOustideTheOffice += numbertotalVisitExecutedOustideTheOffice.intValue();
                                totalVisitExecutedAtTheOffice += numbertotalVisitExecutedAtTheOffice.intValue();
                                totalVisitExecutedPhoneCall += numbertotalVisitExecutedPhoneCall.intValue();

                                totalVisitAwaitingReportOustideTheOffice += numberTotalVisitAwaitingReportOutsideTheOffiche.intValue();
                                totalVisitAwaitingReportAtTheOffice += numberTotalVisitAwaitingReportAtTheOffice.intValue();
                                totalVisitAwaitingReportPhoneCall += numberTotalVisitAwaitingReportPhoneCall.intValue();


                                dashboardVisit.setTotalVisit(totalVisit);

                                dashboardVisit.setUser(userDTO);
                                dashboardVisit.setTotalPlannedVisitAtTheOffice(totalPlannedVisitAtTheOffice);
                                dashboardVisit.setTotalPlannedVisitOustideTheOffice(totalPlannedVisitOustideTheOffice);
                                dashboardVisit.setTotalPlannedVisitPhoneCall(totalPlannedVisitPhoneCall);

                                dashboardVisit.setTotalArchivateVisitAtTheOffice(totalArchivateVisitAtTheOffice);
                                dashboardVisit.setTotalArchivateVisitOustideTheOffice(totalArchivateVisitOustideTheOffice);
                                dashboardVisit.setTotalArchivateVisitPhoneCall(totalArchivateVisitPhoneCall);

                                dashboardVisit.setTotalVisitExecutedAtTheOffice(totalVisitExecutedAtTheOffice);
                                dashboardVisit.setTotalVisitExecutedOustideTheOffice(totalVisitExecutedOustideTheOffice);
                                dashboardVisit.setTotalVisitExecutedPhoneCall(totalVisitExecutedPhoneCall);

                                dashboardVisit.setTotalVisitNotExecutedAtTheOffice(totalVisitNotExecutedAtTheOffice);
                                dashboardVisit.setTotalVisitNotExecutedOustideTheOffice(totalVisitNotExecutedOustideTheOffice);
                                dashboardVisit.setTotalVisitNotExecutedPhoneCall(totalVisitNotExecutedPhoneCall);

                                dashboardVisit.setTotalVisitAwaitingReportAtTheOffice(totalVisitAwaitingReportAtTheOffice);
                                dashboardVisit.setTotalVisitAwaitingReportOustideTheOffice(totalVisitAwaitingReportOustideTheOffice);
                                dashboardVisit.setTotalVisitAwaitingReportPhoneCall(totalVisitAwaitingReportPhoneCall);

                                dashboardVisitBeansList.add(dashboardVisit);
                            }
                        }
                    }
                }

            }
            Collections.sort(dashboardVisitBeansList);
            return dashboardVisitBeansList;
        } else {

            List<UserAfbDTO> userAfbDTOList = afbExternalService.getListUsersDtos();

            if(userList.size() > userAfbDTOList.size()) {
                for(User userDTO : userList){
                    for (UserAfbDTO userAfbDTO : userAfbDTOList){
                        if (userDTO.getUserCode().equals(userAfbDTO.getUserCode())) {

                            dashboardVisit = new DashboardVisitBeans();

                            totalVisit = 0;

                            totalPlannedVisitOustideTheOffice = 0;
                            totalPlannedVisitAtTheOffice = 0;
                            totalPlannedVisitPhoneCall = 0;

                            totalArchivateVisitOustideTheOffice = 0;
                            totalArchivateVisitAtTheOffice = 0;
                            totalArchivateVisitPhoneCall = 0;

                            totalVisitExecutedOustideTheOffice = 0;
                            totalVisitExecutedAtTheOffice = 0;
                            totalVisitExecutedPhoneCall = 0;

                            totalVisitNotExecutedOustideTheOffice = 0;
                            totalVisitNotExecutedAtTheOffice = 0;
                            totalVisitNotExecutedPhoneCall = 0;

                            totalVisitAwaitingReportOustideTheOffice = 0;
                            totalVisitAwaitingReportAtTheOffice = 0;
                            totalVisitAwaitingReportPhoneCall = 0;

                            Long numbertotalVisit = visitsRepository.getNumberTotalVisit(userDTO, dateBegin, dateEnd);

                            Long numbertotalVisitExecutedOustideTheOffice = visitsRepository.getNumberTotalVisitExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numbertotalVisitExecutedAtTheOffice = visitsRepository.getNumberTotalVisitExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numbertotalVisitExecutedPhoneCall = visitsRepository.getNumberTotalVisitExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalPlannedVisitOutsideTheOffiche = visitsRepository.getNumberTotalPlannedVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPlannedVisitAtTheOffice = visitsRepository.getNumberTotalPlannedVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPlannedVisitPhoneCall = visitsRepository.getNumberTotalPlannedVisitPhoneCall(userDTO, dateBegin, dateEnd);


                            Long numberTotalPreparedVisitOutsideTheOffiche = visitsRepository.getNumberTotalArchivateVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPreparedVisitAtTheOffice = visitsRepository.getNumberTotalArchivateVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPreparedVisitPhoneCall = visitsRepository.getNumberTotalArchivateVisitPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalVisitNotExecutedOutsideTheOffiche = visitsRepository.getNumberTotalVisitNotExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitNotExecutedAtTheOffice = visitsRepository.getNumberTotalVisitNotExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitNotExecutedPhoneCall = visitsRepository.getNumberTotalVisitNotExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalVisitAwaitingReportOutsideTheOffiche = visitsRepository.getNumberTotalVisitAwaitingReportOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitAwaitingReportAtTheOffice = visitsRepository.getNumberTotalVisitAwaitingReportAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitAwaitingReportPhoneCall = visitsRepository.getNumberTotalVisitAwaitingReportPhoneCall(userDTO, dateBegin, dateEnd);


                            numbertotalVisit = numbertotalVisit == null ? Long.valueOf(0) : numbertotalVisit;

                            numbertotalVisitExecutedOustideTheOffice = numbertotalVisitExecutedOustideTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedOustideTheOffice;
                            numbertotalVisitExecutedAtTheOffice = numbertotalVisitExecutedAtTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedAtTheOffice;
                            numbertotalVisitExecutedPhoneCall = numbertotalVisitExecutedPhoneCall == null ? Long.valueOf(0) : numbertotalVisitExecutedPhoneCall;

                            numberTotalPlannedVisitOutsideTheOffiche = numberTotalPlannedVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalPlannedVisitOutsideTheOffiche;
                            numberTotalPlannedVisitAtTheOffice = numberTotalPlannedVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalPlannedVisitAtTheOffice;
                            numberTotalPlannedVisitPhoneCall = numberTotalPlannedVisitPhoneCall == null ? Long.valueOf(0) : numberTotalPlannedVisitPhoneCall;

                            numberTotalPreparedVisitOutsideTheOffiche = numberTotalPreparedVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalPreparedVisitOutsideTheOffiche;
                            numberTotalPreparedVisitAtTheOffice = numberTotalPreparedVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalPreparedVisitAtTheOffice;
                            numberTotalPreparedVisitPhoneCall = numberTotalPreparedVisitPhoneCall == null ? Long.valueOf(0) : numberTotalPreparedVisitPhoneCall;

                            numberTotalVisitNotExecutedOutsideTheOffiche = numberTotalVisitNotExecutedOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitNotExecutedOutsideTheOffiche;
                            numberTotalVisitNotExecutedAtTheOffice = numberTotalVisitNotExecutedAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitNotExecutedAtTheOffice;
                            numberTotalVisitNotExecutedPhoneCall = numberTotalVisitNotExecutedPhoneCall == null ? Long.valueOf(0) : numberTotalVisitNotExecutedPhoneCall;

                            numberTotalVisitAwaitingReportOutsideTheOffiche = numberTotalVisitAwaitingReportOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportOutsideTheOffiche;
                            numberTotalVisitAwaitingReportAtTheOffice = numberTotalVisitAwaitingReportAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportAtTheOffice;
                            numberTotalVisitAwaitingReportPhoneCall = numberTotalVisitAwaitingReportPhoneCall == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportPhoneCall;


                            totalVisit += numbertotalVisit.intValue();

                            totalPlannedVisitOustideTheOffice += numberTotalPlannedVisitOutsideTheOffiche.intValue();
                            totalPlannedVisitAtTheOffice += numberTotalPlannedVisitAtTheOffice.intValue();
                            totalPlannedVisitPhoneCall += numberTotalPlannedVisitPhoneCall.intValue();

                            totalArchivateVisitOustideTheOffice += numberTotalPreparedVisitOutsideTheOffiche.intValue();
                            totalArchivateVisitAtTheOffice += numberTotalPreparedVisitAtTheOffice.intValue();
                            totalArchivateVisitPhoneCall += numberTotalPreparedVisitPhoneCall.intValue();

                            totalVisitNotExecutedOustideTheOffice += numberTotalVisitNotExecutedOutsideTheOffiche.intValue();
                            totalVisitNotExecutedAtTheOffice += numberTotalVisitNotExecutedAtTheOffice.intValue();
                            totalVisitNotExecutedPhoneCall += numberTotalVisitNotExecutedPhoneCall.intValue();

                            totalVisitExecutedOustideTheOffice += numbertotalVisitExecutedOustideTheOffice.intValue();
                            totalVisitExecutedAtTheOffice += numbertotalVisitExecutedAtTheOffice.intValue();
                            totalVisitExecutedPhoneCall += numbertotalVisitExecutedPhoneCall.intValue();

                            totalVisitAwaitingReportOustideTheOffice += numberTotalVisitAwaitingReportOutsideTheOffiche.intValue();
                            totalVisitAwaitingReportAtTheOffice += numberTotalVisitAwaitingReportAtTheOffice.intValue();
                            totalVisitAwaitingReportPhoneCall += numberTotalVisitAwaitingReportPhoneCall.intValue();


                            dashboardVisit.setTotalVisit(totalVisit);

                            dashboardVisit.setUser(userDTO);
                            dashboardVisit.setTotalPlannedVisitAtTheOffice(totalPlannedVisitAtTheOffice);
                            dashboardVisit.setTotalPlannedVisitOustideTheOffice(totalPlannedVisitOustideTheOffice);
                            dashboardVisit.setTotalPlannedVisitPhoneCall(totalPlannedVisitPhoneCall);

                            dashboardVisit.setTotalArchivateVisitAtTheOffice(totalArchivateVisitAtTheOffice);
                            dashboardVisit.setTotalArchivateVisitOustideTheOffice(totalArchivateVisitOustideTheOffice);
                            dashboardVisit.setTotalArchivateVisitPhoneCall(totalArchivateVisitPhoneCall);

                            dashboardVisit.setTotalVisitExecutedAtTheOffice(totalVisitExecutedAtTheOffice);
                            dashboardVisit.setTotalVisitExecutedOustideTheOffice(totalVisitExecutedOustideTheOffice);
                            dashboardVisit.setTotalVisitExecutedPhoneCall(totalVisitExecutedPhoneCall);

                            dashboardVisit.setTotalVisitNotExecutedAtTheOffice(totalVisitNotExecutedAtTheOffice);
                            dashboardVisit.setTotalVisitNotExecutedOustideTheOffice(totalVisitNotExecutedOustideTheOffice);
                            dashboardVisit.setTotalVisitNotExecutedPhoneCall(totalVisitNotExecutedPhoneCall);

                            dashboardVisit.setTotalVisitAwaitingReportAtTheOffice(totalVisitAwaitingReportAtTheOffice);
                            dashboardVisit.setTotalVisitAwaitingReportOustideTheOffice(totalVisitAwaitingReportOustideTheOffice);
                            dashboardVisit.setTotalVisitAwaitingReportPhoneCall(totalVisitAwaitingReportPhoneCall);

                            dashboardVisitBeansList.add(dashboardVisit);
                        }
                    }
                }
            } else {
                for (UserAfbDTO userAfbDTO : userAfbDTOList){
                    for(User userDTO : userList){
                        if (userDTO.getUserCode().equals(userAfbDTO.getUserCode())) {

                            dashboardVisit = new DashboardVisitBeans();

                            totalVisit = 0;

                            totalPlannedVisitOustideTheOffice = 0;
                            totalPlannedVisitAtTheOffice = 0;
                            totalPlannedVisitPhoneCall = 0;

                            totalArchivateVisitOustideTheOffice = 0;
                            totalArchivateVisitAtTheOffice = 0;
                            totalArchivateVisitPhoneCall = 0;

                            totalVisitExecutedOustideTheOffice = 0;
                            totalVisitExecutedAtTheOffice = 0;
                            totalVisitExecutedPhoneCall = 0;

                            totalVisitNotExecutedOustideTheOffice = 0;
                            totalVisitNotExecutedAtTheOffice = 0;
                            totalVisitNotExecutedPhoneCall = 0;

                            totalVisitAwaitingReportOustideTheOffice = 0;
                            totalVisitAwaitingReportAtTheOffice = 0;
                            totalVisitAwaitingReportPhoneCall = 0;

                            Long numbertotalVisit = visitsRepository.getNumberTotalVisit(userDTO, dateBegin, dateEnd);

                            Long numbertotalVisitExecutedOustideTheOffice = visitsRepository.getNumberTotalVisitExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numbertotalVisitExecutedAtTheOffice = visitsRepository.getNumberTotalVisitExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numbertotalVisitExecutedPhoneCall = visitsRepository.getNumberTotalVisitExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalPlannedVisitOutsideTheOffiche = visitsRepository.getNumberTotalPlannedVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPlannedVisitAtTheOffice = visitsRepository.getNumberTotalPlannedVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPlannedVisitPhoneCall = visitsRepository.getNumberTotalPlannedVisitPhoneCall(userDTO, dateBegin, dateEnd);


                            Long numberTotalPreparedVisitOutsideTheOffiche = visitsRepository.getNumberTotalArchivateVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPreparedVisitAtTheOffice = visitsRepository.getNumberTotalArchivateVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPreparedVisitPhoneCall = visitsRepository.getNumberTotalArchivateVisitPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalVisitNotExecutedOutsideTheOffiche = visitsRepository.getNumberTotalVisitNotExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitNotExecutedAtTheOffice = visitsRepository.getNumberTotalVisitNotExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitNotExecutedPhoneCall = visitsRepository.getNumberTotalVisitNotExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalVisitAwaitingReportOutsideTheOffiche = visitsRepository.getNumberTotalVisitAwaitingReportOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitAwaitingReportAtTheOffice = visitsRepository.getNumberTotalVisitAwaitingReportAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitAwaitingReportPhoneCall = visitsRepository.getNumberTotalVisitAwaitingReportPhoneCall(userDTO, dateBegin, dateEnd);


                            numbertotalVisit = numbertotalVisit == null ? Long.valueOf(0) : numbertotalVisit;

                            numbertotalVisitExecutedOustideTheOffice = numbertotalVisitExecutedOustideTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedOustideTheOffice;
                            numbertotalVisitExecutedAtTheOffice = numbertotalVisitExecutedAtTheOffice == null ? Long.valueOf(0) : numbertotalVisitExecutedAtTheOffice;
                            numbertotalVisitExecutedPhoneCall = numbertotalVisitExecutedPhoneCall == null ? Long.valueOf(0) : numbertotalVisitExecutedPhoneCall;

                            numberTotalPlannedVisitOutsideTheOffiche = numberTotalPlannedVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalPlannedVisitOutsideTheOffiche;
                            numberTotalPlannedVisitAtTheOffice = numberTotalPlannedVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalPlannedVisitAtTheOffice;
                            numberTotalPlannedVisitPhoneCall = numberTotalPlannedVisitPhoneCall == null ? Long.valueOf(0) : numberTotalPlannedVisitPhoneCall;

                            numberTotalPreparedVisitOutsideTheOffiche = numberTotalPreparedVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalPreparedVisitOutsideTheOffiche;
                            numberTotalPreparedVisitAtTheOffice = numberTotalPreparedVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalPreparedVisitAtTheOffice;
                            numberTotalPreparedVisitPhoneCall = numberTotalPreparedVisitPhoneCall == null ? Long.valueOf(0) : numberTotalPreparedVisitPhoneCall;

                            numberTotalVisitNotExecutedOutsideTheOffiche = numberTotalVisitNotExecutedOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitNotExecutedOutsideTheOffiche;
                            numberTotalVisitNotExecutedAtTheOffice = numberTotalVisitNotExecutedAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitNotExecutedAtTheOffice;
                            numberTotalVisitNotExecutedPhoneCall = numberTotalVisitNotExecutedPhoneCall == null ? Long.valueOf(0) : numberTotalVisitNotExecutedPhoneCall;

                            numberTotalVisitAwaitingReportOutsideTheOffiche = numberTotalVisitAwaitingReportOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportOutsideTheOffiche;
                            numberTotalVisitAwaitingReportAtTheOffice = numberTotalVisitAwaitingReportAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportAtTheOffice;
                            numberTotalVisitAwaitingReportPhoneCall = numberTotalVisitAwaitingReportPhoneCall == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportPhoneCall;


                            totalVisit += numbertotalVisit.intValue();

                            totalPlannedVisitOustideTheOffice += numberTotalPlannedVisitOutsideTheOffiche.intValue();
                            totalPlannedVisitAtTheOffice += numberTotalPlannedVisitAtTheOffice.intValue();
                            totalPlannedVisitPhoneCall += numberTotalPlannedVisitPhoneCall.intValue();

                            totalArchivateVisitOustideTheOffice += numberTotalPreparedVisitOutsideTheOffiche.intValue();
                            totalArchivateVisitAtTheOffice += numberTotalPreparedVisitAtTheOffice.intValue();
                            totalArchivateVisitPhoneCall += numberTotalPreparedVisitPhoneCall.intValue();

                            totalVisitNotExecutedOustideTheOffice += numberTotalVisitNotExecutedOutsideTheOffiche.intValue();
                            totalVisitNotExecutedAtTheOffice += numberTotalVisitNotExecutedAtTheOffice.intValue();
                            totalVisitNotExecutedPhoneCall += numberTotalVisitNotExecutedPhoneCall.intValue();

                            totalVisitExecutedOustideTheOffice += numbertotalVisitExecutedOustideTheOffice.intValue();
                            totalVisitExecutedAtTheOffice += numbertotalVisitExecutedAtTheOffice.intValue();
                            totalVisitExecutedPhoneCall += numbertotalVisitExecutedPhoneCall.intValue();

                            totalVisitAwaitingReportOustideTheOffice += numberTotalVisitAwaitingReportOutsideTheOffiche.intValue();
                            totalVisitAwaitingReportAtTheOffice += numberTotalVisitAwaitingReportAtTheOffice.intValue();
                            totalVisitAwaitingReportPhoneCall += numberTotalVisitAwaitingReportPhoneCall.intValue();


                            dashboardVisit.setTotalVisit(totalVisit);

                            dashboardVisit.setUser(userDTO);
                            dashboardVisit.setTotalPlannedVisitAtTheOffice(totalPlannedVisitAtTheOffice);
                            dashboardVisit.setTotalPlannedVisitOustideTheOffice(totalPlannedVisitOustideTheOffice);
                            dashboardVisit.setTotalPlannedVisitPhoneCall(totalPlannedVisitPhoneCall);

                            dashboardVisit.setTotalArchivateVisitAtTheOffice(totalArchivateVisitAtTheOffice);
                            dashboardVisit.setTotalArchivateVisitOustideTheOffice(totalArchivateVisitOustideTheOffice);
                            dashboardVisit.setTotalArchivateVisitPhoneCall(totalArchivateVisitPhoneCall);

                            dashboardVisit.setTotalVisitExecutedAtTheOffice(totalVisitExecutedAtTheOffice);
                            dashboardVisit.setTotalVisitExecutedOustideTheOffice(totalVisitExecutedOustideTheOffice);
                            dashboardVisit.setTotalVisitExecutedPhoneCall(totalVisitExecutedPhoneCall);

                            dashboardVisit.setTotalVisitNotExecutedAtTheOffice(totalVisitNotExecutedAtTheOffice);
                            dashboardVisit.setTotalVisitNotExecutedOustideTheOffice(totalVisitNotExecutedOustideTheOffice);
                            dashboardVisit.setTotalVisitNotExecutedPhoneCall(totalVisitNotExecutedPhoneCall);

                            dashboardVisit.setTotalVisitAwaitingReportAtTheOffice(totalVisitAwaitingReportAtTheOffice);
                            dashboardVisit.setTotalVisitAwaitingReportOustideTheOffice(totalVisitAwaitingReportOustideTheOffice);
                            dashboardVisit.setTotalVisitAwaitingReportPhoneCall(totalVisitAwaitingReportPhoneCall);

                            dashboardVisitBeansList.add(dashboardVisit);
                        }
                    }
                }
            }

            Collections.sort(dashboardVisitBeansList);

            return dashboardVisitBeansList;
        }
    }

    @Override
    public DashboardVisitBeans getTotalVisitForUserSupervised(String dateBefore, String dateAfter, String periode, UserCriteriaDTO userCriteriaDTO) {
        LocalDate today = LocalDate.now();
        LocalDateTime todayTime = today.atStartOfDay();
        LocalDateTime dateBegin = null;

        LocalDateTime dateEnd = null;

        int totalVisit;

        int numberTotalVisit = 0;
        int numberVisitTotalPlannedVisitOustideTheOffice = 0;
        int numberVisitTotalPlannedVisitAtTheOffice = 0;
        int numberVisitTotalPlannedVisitPhoneCall = 0;
        int totalnumberVisitTotalPlannedVisit = 0;
        int numberVisitTotalArchivateVisitOustideTheOffice = 0;
        int numberVisitTotalArchivateVisitAtTheOffice = 0;
        int numberVisitTotalArchivateVisitPhoneCall = 0;
        int totalnumberVisitTotalArchivateVisit = 0;
        int numberVisitTotalVisitNotExecutedOustideTheOffice = 0;
        int numberVisitTotalVisitNotExecutedAtTheOffice = 0;
        int numberVisitTotalVisitNotExecutedPhoneCall = 0;
        int totalnumberVisitTotalVisitNotExecuted = 0;
        int numberVisitTotalVisitAwaitingReportOustideTheOffice = 0;
        int numberVisitTotalVisitAwaitingReportAtTheOffice = 0;
        int numberVisitTotalVisitAwaitingReportPhoneCall = 0;
        int totalnumberVisitTotalVisitAwaitingReport = 0;

        int totalPlannedVisitOustideTheOffice;
        int totalPlannedVisitAtTheOffice;
        int totalPlannedVisitPhoneCall;

        int totalArchivateVisitOustideTheOffice;
        int totalArchivateVisitAtTheOffice;
        int totalArchivateVisitPhoneCall;

        int totalVisitNotExecutedOustideTheOffice;
        int totalVisitNotExecutedAtTheOffice;
        int totalVisitNotExecutedPhoneCall;

        int totalVisitAwaitingReportOustideTheOffice;
        int totalVisitAwaitingReportAtTheOffice;
        int totalVisitAwaitingReportPhoneCall;

        if (MethoUtils.WEEK.equals(periode)) {

            TemporalField field  = WeekFields.of(Locale.FRANCE).dayOfWeek();

            dateBegin = todayTime.with(field, 1);
            dateEnd = LocalDate.now().atTime(LocalTime.MAX).with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        } else if (MethoUtils.MONTH.equals(periode)) {

            dateBegin = todayTime.with(TemporalAdjusters.firstDayOfMonth());
            dateEnd = LocalDate.now().atTime(LocalTime.MAX).with(TemporalAdjusters.lastDayOfMonth());

        } else if (MethoUtils.QUARTERLY.equals(periode)) {

            dateBegin = today.minusMonths(3).atStartOfDay();
            dateEnd = LocalDate.now().atTime(LocalTime.MAX);

        } else if (MethoUtils.BIANNUAL.equals(periode)) {

            dateBegin = today.minusMonths(6).atStartOfDay();
            dateEnd = LocalDate.now().atTime(LocalTime.MAX);

        } else if (MethoUtils.YEAR.equals(periode)) {

            dateBegin = todayTime.with(TemporalAdjusters.firstDayOfYear());
            dateEnd = LocalDate.now().atTime(LocalTime.MAX).with(TemporalAdjusters.lastDayOfYear());

        }
        else {
            dateBegin = MethoUtils.INDEFINI_TEXT.equalsIgnoreCase(dateBefore) ? null
                    : MethoUtils.convertorDateTime(dateBefore);

            dateEnd = MethoUtils.INDEFINI_TEXT.equalsIgnoreCase(dateAfter) ? null
                    : MethoUtils.convertorDateTime(dateAfter);
        }

        User user = userService.getCurrentUser();
        List<User> userList = userService.getAllUsersList(userCriteriaDTO);

        List<UserAfbDTO> userAfbDTOList = afbExternalService.getAllUserSupervisedByUSerCode(user.getUserCode());

        if (!userAfbDTOList.isEmpty()) {
            if (userList.size() > userAfbDTOList.size()) {
                for (User userDTO : userList) {
                    for (UserAfbDTO userAfbDTO : userAfbDTOList) {
                        if (userDTO.getUserCode().equals(userAfbDTO.getUserCode())) {

                            totalVisit = 0;

                            totalPlannedVisitOustideTheOffice = 0;
                            totalPlannedVisitAtTheOffice = 0;
                            totalPlannedVisitPhoneCall = 0;

                            totalArchivateVisitOustideTheOffice = 0;
                            totalArchivateVisitAtTheOffice = 0;
                            totalArchivateVisitPhoneCall = 0;

                            totalVisitNotExecutedOustideTheOffice = 0;
                            totalVisitNotExecutedAtTheOffice = 0;
                            totalVisitNotExecutedPhoneCall = 0;

                            totalVisitAwaitingReportOustideTheOffice = 0;
                            totalVisitAwaitingReportAtTheOffice = 0;
                            totalVisitAwaitingReportPhoneCall = 0;

                            Long numbertotalVisit = visitsRepository.getNumberTotalVisit(userDTO, dateBegin, dateEnd);

                            Long numberTotalPlannedVisitOutsideTheOffiche = visitsRepository.getNumberTotalPlannedVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPlannedVisitAtTheOffice = visitsRepository.getNumberTotalPlannedVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPlannedVisitPhoneCall = visitsRepository.getNumberTotalPlannedVisitPhoneCall(userDTO, dateBegin, dateEnd);


                            Long numberTotalArchivateVisitOutsideTheOffiche = visitsRepository.getNumberTotalArchivateVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalArchivateVisitAtTheOffice = visitsRepository.getNumberTotalArchivateVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalArchivateVisitPhoneCall = visitsRepository.getNumberTotalArchivateVisitPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalVisitNotExecutedOutsideTheOffiche = visitsRepository.getNumberTotalVisitNotExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitNotExecutedAtTheOffice = visitsRepository.getNumberTotalVisitNotExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitNotExecutedPhoneCall = visitsRepository.getNumberTotalVisitNotExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalVisitAwaitingReportOutsideTheOffiche = visitsRepository.getNumberTotalVisitAwaitingReportOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitAwaitingReportAtTheOffice = visitsRepository.getNumberTotalVisitAwaitingReportAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitAwaitingReportPhoneCall = visitsRepository.getNumberTotalVisitAwaitingReportPhoneCall(userDTO, dateBegin, dateEnd);


                            numbertotalVisit = numbertotalVisit == null ? Long.valueOf(0) : numbertotalVisit;

                            numberTotalPlannedVisitOutsideTheOffiche = numberTotalPlannedVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalPlannedVisitOutsideTheOffiche;
                            numberTotalPlannedVisitAtTheOffice = numberTotalPlannedVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalPlannedVisitAtTheOffice;
                            numberTotalPlannedVisitPhoneCall = numberTotalPlannedVisitPhoneCall == null ? Long.valueOf(0) : numberTotalPlannedVisitPhoneCall;

                            numberTotalArchivateVisitOutsideTheOffiche = numberTotalArchivateVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalArchivateVisitOutsideTheOffiche;
                            numberTotalArchivateVisitAtTheOffice = numberTotalArchivateVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalArchivateVisitAtTheOffice;
                            numberTotalArchivateVisitPhoneCall = numberTotalArchivateVisitPhoneCall == null ? Long.valueOf(0) : numberTotalArchivateVisitPhoneCall;

                            numberTotalVisitNotExecutedOutsideTheOffiche = numberTotalVisitNotExecutedOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitNotExecutedOutsideTheOffiche;
                            numberTotalVisitNotExecutedAtTheOffice = numberTotalVisitNotExecutedAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitNotExecutedAtTheOffice;
                            numberTotalVisitNotExecutedPhoneCall = numberTotalVisitNotExecutedPhoneCall == null ? Long.valueOf(0) : numberTotalVisitNotExecutedPhoneCall;

                            numberTotalVisitAwaitingReportOutsideTheOffiche = numberTotalVisitAwaitingReportOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportOutsideTheOffiche;
                            numberTotalVisitAwaitingReportAtTheOffice = numberTotalVisitAwaitingReportAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportAtTheOffice;
                            numberTotalVisitAwaitingReportPhoneCall = numberTotalVisitAwaitingReportPhoneCall == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportPhoneCall;


                            totalVisit += numbertotalVisit.intValue();

                            totalPlannedVisitOustideTheOffice += numberTotalPlannedVisitOutsideTheOffiche.intValue();
                            totalPlannedVisitAtTheOffice += numberTotalPlannedVisitAtTheOffice.intValue();
                            totalPlannedVisitPhoneCall += numberTotalPlannedVisitPhoneCall.intValue();

                            totalArchivateVisitOustideTheOffice += numberTotalArchivateVisitOutsideTheOffiche.intValue();
                            totalArchivateVisitAtTheOffice += numberTotalArchivateVisitAtTheOffice.intValue();
                            totalArchivateVisitPhoneCall += numberTotalArchivateVisitPhoneCall.intValue();

                            totalVisitNotExecutedOustideTheOffice += numberTotalVisitNotExecutedOutsideTheOffiche.intValue();
                            totalVisitNotExecutedAtTheOffice += numberTotalVisitNotExecutedAtTheOffice.intValue();
                            totalVisitNotExecutedPhoneCall += numberTotalVisitNotExecutedPhoneCall.intValue();

                            totalVisitAwaitingReportOustideTheOffice += numberTotalVisitAwaitingReportOutsideTheOffiche.intValue();
                            totalVisitAwaitingReportAtTheOffice += numberTotalVisitAwaitingReportAtTheOffice.intValue();
                            totalVisitAwaitingReportPhoneCall += numberTotalVisitAwaitingReportPhoneCall.intValue();

                            numberTotalVisit += totalVisit;
                            numberVisitTotalPlannedVisitOustideTheOffice += totalPlannedVisitOustideTheOffice;
                            numberVisitTotalPlannedVisitAtTheOffice += totalPlannedVisitAtTheOffice;
                            numberVisitTotalPlannedVisitPhoneCall += totalPlannedVisitPhoneCall;
                            totalnumberVisitTotalPlannedVisit += (totalPlannedVisitOustideTheOffice + totalPlannedVisitAtTheOffice + totalPlannedVisitPhoneCall);
                            numberVisitTotalVisitNotExecutedAtTheOffice += totalVisitNotExecutedAtTheOffice;
                            numberVisitTotalVisitNotExecutedOustideTheOffice += totalVisitNotExecutedOustideTheOffice;
                            numberVisitTotalVisitNotExecutedPhoneCall += totalVisitNotExecutedPhoneCall;
                            totalnumberVisitTotalVisitNotExecuted += (totalVisitNotExecutedAtTheOffice + totalVisitNotExecutedOustideTheOffice + totalVisitNotExecutedPhoneCall);
                            numberVisitTotalVisitAwaitingReportOustideTheOffice += totalVisitAwaitingReportOustideTheOffice;
                            numberVisitTotalVisitAwaitingReportAtTheOffice += totalVisitAwaitingReportAtTheOffice;
                            numberVisitTotalVisitAwaitingReportPhoneCall += totalVisitAwaitingReportPhoneCall;
                            totalnumberVisitTotalVisitAwaitingReport += (totalVisitAwaitingReportOustideTheOffice + totalVisitAwaitingReportAtTheOffice + totalVisitAwaitingReportPhoneCall);
                            numberVisitTotalArchivateVisitAtTheOffice += totalArchivateVisitAtTheOffice;
                            numberVisitTotalArchivateVisitOustideTheOffice += totalArchivateVisitOustideTheOffice;
                            numberVisitTotalArchivateVisitPhoneCall += totalArchivateVisitPhoneCall;
                            totalnumberVisitTotalArchivateVisit += (totalArchivateVisitAtTheOffice + totalArchivateVisitOustideTheOffice + totalArchivateVisitPhoneCall);

                        }
                    }
                }
            } else {
                for (UserAfbDTO userAfbDTO : userAfbDTOList) {
                    for (User userDTO : userList) {
                        if (userDTO.getUserCode().equals(userAfbDTO.getUserCode())) {

                            totalVisit = 0;

                            totalPlannedVisitOustideTheOffice = 0;
                            totalPlannedVisitAtTheOffice = 0;
                            totalPlannedVisitPhoneCall = 0;

                            totalArchivateVisitOustideTheOffice = 0;
                            totalArchivateVisitAtTheOffice = 0;
                            totalArchivateVisitPhoneCall = 0;

                            totalVisitNotExecutedOustideTheOffice = 0;
                            totalVisitNotExecutedAtTheOffice = 0;
                            totalVisitNotExecutedPhoneCall = 0;

                            totalVisitAwaitingReportOustideTheOffice = 0;
                            totalVisitAwaitingReportAtTheOffice = 0;
                            totalVisitAwaitingReportPhoneCall = 0;

                            Long numbertotalVisit = visitsRepository.getNumberTotalVisit(userDTO, dateBegin, dateEnd);

                            Long numberTotalPlannedVisitOutsideTheOffiche = visitsRepository.getNumberTotalPlannedVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPlannedVisitAtTheOffice = visitsRepository.getNumberTotalPlannedVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalPlannedVisitPhoneCall = visitsRepository.getNumberTotalPlannedVisitPhoneCall(userDTO, dateBegin, dateEnd);


                            Long numberTotalArchivateVisitOutsideTheOffiche = visitsRepository.getNumberTotalArchivateVisitOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalArchivateVisitAtTheOffice = visitsRepository.getNumberTotalArchivateVisitAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalArchivateVisitPhoneCall = visitsRepository.getNumberTotalArchivateVisitPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalVisitNotExecutedOutsideTheOffiche = visitsRepository.getNumberTotalVisitNotExecutedOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitNotExecutedAtTheOffice = visitsRepository.getNumberTotalVisitNotExecutedAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitNotExecutedPhoneCall = visitsRepository.getNumberTotalVisitNotExecutedPhoneCall(userDTO, dateBegin, dateEnd);

                            Long numberTotalVisitAwaitingReportOutsideTheOffiche = visitsRepository.getNumberTotalVisitAwaitingReportOutsideTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitAwaitingReportAtTheOffice = visitsRepository.getNumberTotalVisitAwaitingReportAtTheOffiche(userDTO, dateBegin, dateEnd);
                            Long numberTotalVisitAwaitingReportPhoneCall = visitsRepository.getNumberTotalVisitAwaitingReportPhoneCall(userDTO, dateBegin, dateEnd);


                            numbertotalVisit = numbertotalVisit == null ? Long.valueOf(0) : numbertotalVisit;

                            numberTotalPlannedVisitOutsideTheOffiche = numberTotalPlannedVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalPlannedVisitOutsideTheOffiche;
                            numberTotalPlannedVisitAtTheOffice = numberTotalPlannedVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalPlannedVisitAtTheOffice;
                            numberTotalPlannedVisitPhoneCall = numberTotalPlannedVisitPhoneCall == null ? Long.valueOf(0) : numberTotalPlannedVisitPhoneCall;

                            numberTotalArchivateVisitOutsideTheOffiche = numberTotalArchivateVisitOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalArchivateVisitOutsideTheOffiche;
                            numberTotalArchivateVisitAtTheOffice = numberTotalArchivateVisitAtTheOffice == null ? Long.valueOf(0) : numberTotalArchivateVisitAtTheOffice;
                            numberTotalArchivateVisitPhoneCall = numberTotalArchivateVisitPhoneCall == null ? Long.valueOf(0) : numberTotalArchivateVisitPhoneCall;

                            numberTotalVisitNotExecutedOutsideTheOffiche = numberTotalVisitNotExecutedOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitNotExecutedOutsideTheOffiche;
                            numberTotalVisitNotExecutedAtTheOffice = numberTotalVisitNotExecutedAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitNotExecutedAtTheOffice;
                            numberTotalVisitNotExecutedPhoneCall = numberTotalVisitNotExecutedPhoneCall == null ? Long.valueOf(0) : numberTotalVisitNotExecutedPhoneCall;

                            numberTotalVisitAwaitingReportOutsideTheOffiche = numberTotalVisitAwaitingReportOutsideTheOffiche == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportOutsideTheOffiche;
                            numberTotalVisitAwaitingReportAtTheOffice = numberTotalVisitAwaitingReportAtTheOffice == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportAtTheOffice;
                            numberTotalVisitAwaitingReportPhoneCall = numberTotalVisitAwaitingReportPhoneCall == null ? Long.valueOf(0) : numberTotalVisitAwaitingReportPhoneCall;


                            totalVisit += numbertotalVisit.intValue();

                            totalPlannedVisitOustideTheOffice += numberTotalPlannedVisitOutsideTheOffiche.intValue();
                            totalPlannedVisitAtTheOffice += numberTotalPlannedVisitAtTheOffice.intValue();
                            totalPlannedVisitPhoneCall += numberTotalPlannedVisitPhoneCall.intValue();

                            totalArchivateVisitOustideTheOffice += numberTotalArchivateVisitOutsideTheOffiche.intValue();
                            totalArchivateVisitAtTheOffice += numberTotalArchivateVisitAtTheOffice.intValue();
                            totalArchivateVisitPhoneCall += numberTotalArchivateVisitPhoneCall.intValue();

                            totalVisitNotExecutedOustideTheOffice += numberTotalVisitNotExecutedOutsideTheOffiche.intValue();
                            totalVisitNotExecutedAtTheOffice += numberTotalVisitNotExecutedAtTheOffice.intValue();
                            totalVisitNotExecutedPhoneCall += numberTotalVisitNotExecutedPhoneCall.intValue();

                            totalVisitAwaitingReportOustideTheOffice += numberTotalVisitAwaitingReportOutsideTheOffiche.intValue();
                            totalVisitAwaitingReportAtTheOffice += numberTotalVisitAwaitingReportAtTheOffice.intValue();
                            totalVisitAwaitingReportPhoneCall += numberTotalVisitAwaitingReportPhoneCall.intValue();

                            numberTotalVisit += totalVisit;
                            numberVisitTotalPlannedVisitOustideTheOffice += totalPlannedVisitOustideTheOffice;
                            numberVisitTotalPlannedVisitAtTheOffice += totalPlannedVisitAtTheOffice;
                            numberVisitTotalPlannedVisitPhoneCall += totalPlannedVisitPhoneCall;
                            totalnumberVisitTotalPlannedVisit += (totalPlannedVisitOustideTheOffice + totalPlannedVisitAtTheOffice + totalPlannedVisitPhoneCall);
                            numberVisitTotalVisitNotExecutedAtTheOffice += totalVisitNotExecutedAtTheOffice;
                            numberVisitTotalVisitNotExecutedOustideTheOffice += totalVisitNotExecutedOustideTheOffice;
                            numberVisitTotalVisitNotExecutedPhoneCall += totalVisitNotExecutedPhoneCall;
                            totalnumberVisitTotalVisitNotExecuted += (totalVisitNotExecutedAtTheOffice + totalVisitNotExecutedOustideTheOffice + totalVisitNotExecutedPhoneCall);
                            numberVisitTotalVisitAwaitingReportOustideTheOffice += totalVisitAwaitingReportOustideTheOffice;
                            numberVisitTotalVisitAwaitingReportAtTheOffice += totalVisitAwaitingReportAtTheOffice;
                            numberVisitTotalVisitAwaitingReportPhoneCall += totalVisitAwaitingReportPhoneCall;
                            totalnumberVisitTotalVisitAwaitingReport += (totalVisitAwaitingReportOustideTheOffice + totalVisitAwaitingReportAtTheOffice + totalVisitAwaitingReportPhoneCall);
                            numberVisitTotalArchivateVisitAtTheOffice += totalArchivateVisitAtTheOffice;
                            numberVisitTotalArchivateVisitOustideTheOffice += totalArchivateVisitOustideTheOffice;
                            numberVisitTotalArchivateVisitPhoneCall += totalArchivateVisitPhoneCall;
                            totalnumberVisitTotalArchivateVisit += (totalArchivateVisitAtTheOffice + totalArchivateVisitOustideTheOffice + totalArchivateVisitPhoneCall);
                        }
                    }
                }
            }

        }

        DashboardVisitBeans dashboardVisitBeans = new DashboardVisitBeans();

        dashboardVisitBeans.setNumberTotalVisitForUser(numberTotalVisit);
        dashboardVisitBeans.setNumberTotalArchivateVisitAtTheOffice(numberVisitTotalArchivateVisitAtTheOffice);
        dashboardVisitBeans.setNumberTotalArchivateVisitOustideTheOffice(numberVisitTotalArchivateVisitOustideTheOffice);
        dashboardVisitBeans.setNumberTotalArchivateVisitPhoneCall(numberVisitTotalArchivateVisitPhoneCall);
        dashboardVisitBeans.setNumberTotalPlannedVisitAtTheOffice(numberVisitTotalPlannedVisitAtTheOffice);
        dashboardVisitBeans.setNumberTotalPlannedVisitOustideTheOffice(numberVisitTotalPlannedVisitOustideTheOffice);
        dashboardVisitBeans.setNumberTotalPlannedVisitPhoneCall(numberVisitTotalPlannedVisitPhoneCall);
        dashboardVisitBeans.setNumberTotalVisitAwaitingReportAtTheOffice(numberVisitTotalVisitAwaitingReportAtTheOffice);
        dashboardVisitBeans.setNumberTotalVisitAwaitingReportOustideTheOffice(numberVisitTotalVisitAwaitingReportOustideTheOffice);
        dashboardVisitBeans.setNumberTotalVisitAwaitingReportPhoneCall(numberVisitTotalVisitAwaitingReportPhoneCall);
        dashboardVisitBeans.setNumberTotalVisitExecutedAtTheOffice(numberVisitTotalVisitNotExecutedAtTheOffice);
        dashboardVisitBeans.setNumberTotalVisitExecutedOustideTheOffice(numberVisitTotalVisitNotExecutedOustideTheOffice);
        dashboardVisitBeans.setNumberTotalVisitExecutedPhoneCall(numberVisitTotalVisitNotExecutedPhoneCall);
        dashboardVisitBeans.setNumberTotalVisitNotExecutedAtTheOffice(numberVisitTotalVisitNotExecutedAtTheOffice);
        dashboardVisitBeans.setNumberTotalVisitNotExecutedOustideTheOffice(numberVisitTotalVisitNotExecutedOustideTheOffice);
        dashboardVisitBeans.setNumberTotalVisitNotExecutedPhoneCall(numberVisitTotalVisitNotExecutedPhoneCall);
        dashboardVisitBeans.setTotalPlannedVisit(totalnumberVisitTotalPlannedVisit);
        dashboardVisitBeans.setTotalAwaitingReportVisit(totalnumberVisitTotalVisitAwaitingReport);
        dashboardVisitBeans.setTotalArchivateVisit(totalnumberVisitTotalArchivateVisit);
        dashboardVisitBeans.setTotalVisitNotExecuted(totalnumberVisitTotalVisitNotExecuted);

        return dashboardVisitBeans;
    }

    private void sendMailVisit(Long value, Visits v, List<VisitParticipants> visitParticipants) {
        participants = new String[visitParticipants.size()];
        int i = 0;

        for (VisitParticipants visitParticipants1 : visitParticipants){
            if (visitParticipants1.getVisit().getUserAfb().getEmail() != null || v.getUserAfb().getEmail() != null) {
                participants[i] = visitParticipants1.getUser().getEmail();
                i = i + 1;
            }

        }

        mailService.sendEmail(v.getUserAfb().getEmail(), participants, "RAPPEL VISITE PLANIFIEE", "Nom du Client : <b>" + v.getClient().getFirstName() + " " + v.getClient().getLastName() + "</b><br/> " +
                    "Date de la visite : <b>" + v.getVisitDateText() + "</b><br/>" +
                    "Date de création : <b>" + v.getDateCreatedText() + "</b><br/>" +
                    "Type de visite : <b>" + v.getVisitType() + "</b><br/>" +
                    "Moyen utilisé : <b>" + v.getMoyenUtilise() + "</b><br/><br/>" +
                    "Nombre de jours restants pour la visite : <b>0" + value + " jours restants</b><br/>", true, true);
    }

    private void sendNotificationVisit(Visits v, Long value, List<VisitParticipants> visitParticipants) {

        if(visitParticipants != null) {

            for (VisitParticipants visitParticipants1 : visitParticipants){

                Notifications notifications = new Notifications();
                notifications.setSendTo(visitParticipants1.getUser());
                notifications.setCreateDate(LocalDateTime.now());
                notifications.setStatus(false);
                notifications.setCreateBy(v.getUserAfb());
                notifications.setTypeNotification("RAPPEL VISITE PLANIFIEE");
                notifications.setDescriptionNotification("Nom du Client : <b>" + v.getClient().getFirstName() + " " + v.getClient().getLastName() + "</b><br/> " +
                        "Date de la visite : <b>" + v.getVisitDateText() + "</b><br/>" +
                        "Date de création : <b>" + v.getDateCreatedText() + "</b><br/>" +
                        "Type de visite : <b>" + v.getVisitType() + "</b><br/>" +
                        "Moyen utilisé : <b>" + v.getMoyenUtilise() + "</b><br/>" +
                        "Nombre de jours restants pour la visite : <b>0" + value + " jours restants</b><br/>");

            }
        }

        Notifications notifications = new Notifications();
        notifications.setSendTo(v.getUserAfb());
        notifications.setCreateDate(LocalDateTime.now());
        notifications.setStatus(false);
        notifications.setCreateBy(v.getUserAfb());
        notifications.setTypeNotification("RAPPEL VISITE PLANIFIEE");
        notifications.setDescriptionNotification("Nom du Client : <b>" + v.getClient().getFirstName() + " " + v.getClient().getLastName() + "</b><br/> " +
                    "Date de la visite : <b>" + v.getVisitDateText() + "</b><br/>" +
                    "Date de création : <b>" + v.getDateCreatedText() + "</b><br/>" +
                    "Type de visite : <b>" + v.getVisitType() + "</b><br/>" +
                    "Moyen utilisé : <b>" + v.getMoyenUtilise() + "</b><br/><br/>" +
                    "Nombre de jours restants pour la visite : <b>0" + value + " jours restants</b><br/>");

        notificationRepository.save(notifications);
    }

    private void sendNotificationVisitJJ(Visits v, List<VisitParticipants> visitParticipants) {

        if(visitParticipants != null) {

            for (VisitParticipants visitParticipants1 : visitParticipants){
                Notifications notifications = new Notifications();
                notifications.setSendTo(visitParticipants1.getUser());
                notifications.setCreateDate(LocalDateTime.now());
                notifications.setStatus(false);
                notifications.setCreateBy(v.getUserAfb());
                notifications.setTypeNotification("RAPPEL VISITE PLANIFIEE");
                notifications.setDescriptionNotification("Nom du Client : <b>" + v.getClient().getFirstName() + " " + v.getClient().getLastName() + "</b><br/> " +
                        "Date de la visite : <b>" + v.getVisitDateText() + "</b><br/>" +
                        "Date de création : <b>" + v.getDateCreatedText() + "</b><br/>" +
                        "Type de visite : <b>" + v.getVisitType() + "</b><br/>" +
                        "Moyen utilisé : <b>" + v.getMoyenUtilise() + "</b><br/>" +
                        "<b>LA VISITE A LIEU AUJOURD'HUI</b><br/>");
                notificationRepository.save(notifications);

            }
        }

        Notifications notifications = new Notifications();
        notifications.setSendTo(v.getUserAfb());
        notifications.setCreateDate(LocalDateTime.now());
        notifications.setStatus(false);
        notifications.setCreateBy(v.getUserAfb());
        notifications.setTypeNotification("RAPPEL VISITE PLANIFIEE");
        notifications.setDescriptionNotification("Nom du Client : <b>" + v.getClient().getFirstName() + " " + v.getClient().getLastName() + "</b><br/> " +
                "Date de la visite : <b>" + v.getVisitDateText() + "</b><br/>" +
                "Date de création : <b>" + v.getDateCreatedText() + "</b><br/>" +
                "Type de visite : <b>" + v.getVisitType() + "</b><br/>" +
                "Moyen utilisé : <b>" + v.getMoyenUtilise() + "</b><br/><br/>" +
                "<b>LA VISITE A LIEU AUJOURD'HUI</b><br/>");

        notificationRepository.save(notifications);
    }

    private void sendMailVisitJJ(Visits v, List<VisitParticipants> visitParticipants) {

        participants = new String[visitParticipants.size()];
        int i = 0;

        for (VisitParticipants visitParticipants1 : visitParticipants){
            userParticipants += visitParticipants1.getUser().getFirstName() + " " + visitParticipants1.getUser().getLastName() + ", ";

            if (visitParticipants1.getVisit().getUserAfb().getEmail() != null || v.getUserAfb().getEmail() != null) {
                participants[i] = visitParticipants1.getUser().getEmail();
                i = i + 1;
            }

        }

        mailService.sendEmail(v.getUserAfb().getEmail(), participants, "RAPPEL VISITE PLANIFIEE", "Nom du Client : <b>" + v.getClient().getFirstName() + " " + v.getClient().getLastName() + "</b><br/> " +
                "Date de la visite : <b>" + v.getVisitDateText() + "</b><br/>" +
                "Date de création : <b>" + v.getDateCreatedText() + "</b><br/>" +
                "Type de visite : <b>" + v.getVisitType() + "</b><br/>" +
                "Moyen utilisé : <b>" + v.getMoyenUtilise() + "</b><br/><br/>" +
                "<b>LA VISITE A LIEU AUJOURD'HUI</b><br/>", true, true);

    }


    /**
     * delete visit.
     *
     * @param visit the visit
     */
    @Transactional
    @Override
    public void delete(Visits visit) {
        LOGGER.debug("REQ - delete visit");
        visitsRepository.delete(visit);
    }

    /**
     * delete visit by Id.
     *
     * @param id the id.
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        LOGGER.debug("REQ - delete visit by id");
        visitsRepository.deleteById(id);
    }

    public void sendMail(User user, String subject, String content) {
        if(user.getEmail() != null) {
            mailService.sendEmail(user.getEmail(), subject,
                    content, true, true);
        }
    }

    private static String patternNotification(String text, Visits visits, User user) {

        text = text.replace("{NOM_CLIENT}", visits.getClient().getFirstName().toUpperCase() + " " + visits.getClient().getLastName().toUpperCase());
        text = text.replace("{DATE_VISITE}", visits.getVisitDateText());
        text = text.replace("{NOM_EXPEDITEUR}", user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
        return text;
    }

    private static String patternNotificationWithParticipant(String text, Visits visits, User user, String userParticipants) {

        text = text
                .replace("{NOM_CLIENT}", visits.getClient().getFirstName().toUpperCase()
                        + " " + visits.getClient().getLastName().toUpperCase());
        text = text.replace("{DATE_VISITE}", visits.getVisitDateText());
        text = text.replace("{NOM_EXPEDITEUR}", user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
        text = text.replace("{{NOM_PARTICIPANT}}", userParticipants);
        return text;
    }
}
