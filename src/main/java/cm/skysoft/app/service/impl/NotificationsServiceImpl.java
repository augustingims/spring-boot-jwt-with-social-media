package cm.skysoft.app.service.impl;

import cm.skysoft.app.criteria.NotificationCriteriaDTO;
import cm.skysoft.app.domain.Notifications;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.dto.NotificationDTO;
import cm.skysoft.app.dto.UserAfbDTO;
import cm.skysoft.app.repository.NotificationRepository;
import cm.skysoft.app.repository.specification.NotificationSpecification;
import cm.skysoft.app.service.AfbExternalService;
import cm.skysoft.app.service.NotificationsService;
import cm.skysoft.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/25/21.
 */
@Service
public class NotificationsServiceImpl implements NotificationsService {

    private final Logger LOGGER = LoggerFactory.getLogger(VisitsServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final AfbExternalService afbExternalService;
    private final UserService userService;
    private static final String PLANIFICATION = "PLANIFICATION VISITE";
    private String query = "";
    private final EntityManager em;

    public NotificationsServiceImpl(NotificationRepository notificationRepository, AfbExternalService afbExternalService, UserService userService, EntityManager em) {
        this.notificationRepository = notificationRepository;
        this.afbExternalService = afbExternalService;
        this.userService = userService;
        this.em = em;
    }

    /**
     * save Notifications.
     *
     * @param notificationDTO the notificationDTO
     */
    @Override
    public Notifications save(NotificationDTO notificationDTO) {

        LOGGER.debug("REQ - save visitDTO "+ notificationDTO);

        Notifications notification = new Notifications();
        User user = userService.getCurrentUser();

        List<UserAfbDTO> userAfbDTOs = afbExternalService.getAllUserSupervisorsByUSerCode(user.getUserCode());

        for (UserAfbDTO userAfbDTO: userAfbDTOs) {

            Optional<User> u = userService.getUserByIdUserAfb(userAfbDTO.getId());

            if (u.isPresent()) {
                notification.setTypeNotification(PLANIFICATION);
                notification.setCreateBy(user);
                notification.setStatus(notificationDTO.isStatus());
                notification.setCreateDate(LocalDateTime.now());
                notification.setDescriptionNotification(notificationDTO.getDescriptionNotification());
                notification.setSendTo(u.get());
                notification = notificationRepository.save(notification);
            }
        }




        return notification;
    }

    /**
     * update notification.
     *
     * @param notificationDTO the notificationDTO
     */
    @Override
    public Notifications update(NotificationDTO notificationDTO) {

        Optional<Notifications> notification = getNotificationById(notificationDTO.getId());

        notification.ifPresent(notifications -> {
            notifications.setDescriptionNotification(notificationDTO.getDescriptionNotification());
            notifications.setTypeNotification(notificationDTO.getTypeNotification());
            notifications.setStatus(notificationDTO.isStatus());
        });

        return notification.map(notificationRepository::save).orElse(null);
    }

    @Override
    public void updateNotification(Long idNotification) {
        if(idNotification != null){
            Optional<Notifications> notification = getNotificationById(idNotification);

            notification.ifPresent(notifications -> notifications.setStatus(Boolean.TRUE));

            NotificationDTO notificationDTO = new NotificationDTO();

            notification.ifPresent(notifications -> notificationDTO.setId(notifications.getId()));
            notification.ifPresent(notifications -> notificationDTO.setCreateDate(notifications.getCreateDate()));
            notification.ifPresent(notifications -> notificationDTO.setDescriptionNotification(notifications.getDescriptionNotification()));
            notification.ifPresent(notifications -> notificationDTO.setStatus(notifications.getStatus()));
            notification.ifPresent(notifications -> notificationDTO.setTypeNotification(notifications.getTypeNotification()));

            update(notificationDTO);
        }
    }

    /**
     * get suggestion by id.
     *
     * @param id the id
     * @return notification found.
     */
    @Override
    public Optional<Notifications> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    /**
     * get all Notifications.
     *
     * @param pageable the pageable
     * @return list of notification found.
     */
    @Override
    public Page<Notifications> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    /**
     * get all notifications.
     *
     * @return list of notifications found.
     */
    @Override
    public List<Notifications> getAllNotifications(Boolean status, Long idUser, Long sentTo, Long resultmax, LocalDateTime dateDebut, LocalDateTime dateFin) {

        Query q = null;

        query = " select n from Notifications n where 1=1 ";

        if(status != null){
            query += " and n.status = :status ";
        }
        if(idUser != null){
            query += " and n.createBy.id = :idUser ";
        }
        if(sentTo != null){
            query += " and n.sendTo.id = :sentTo ";
        }
        if(dateDebut != null){
            query += " and n.createDate >= :dateDebut ";
        }
        if(dateFin != null){
            query += " and n.createDate <= :dateFin ";
        }
        if(dateDebut != null &&  dateFin != null){
            query += " and n.createDate between :dateDebut and :dateFin ";
        }

        query += " order by n.createDate desc ";

        q = em.createQuery(query);

        if(status != null){
            q.setParameter("status", status);
        }
        if(idUser != null){
            q.setParameter("idUser", idUser);
        }
        if(sentTo != null){
            q.setParameter("sentTo", sentTo);
        }
        if(resultmax != null){
            q.setMaxResults(resultmax.intValue());
        }
        if(dateDebut != null){
            q.setParameter("dateDebut", dateDebut);
        }
        if(dateFin != null){
            q.setParameter("dateFin", dateFin);
        }

        return q.getResultList();
    }

    /**
     * get all notifications.
     *
     * @return list of notifications found.
     */
    @Override
    public List<Notifications> getAllNotificationsBySendToUser() {
        User user = userService.getCurrentUser();
        return notificationRepository.getNotificationsBySendToUser(user.getId());
    }

    /**
     * get all notifications.
     *
     * @return list of notifications found.
     */
    @Override
    public List<Notifications> getAllNotificationsBySendToUserStatusTrue() {
        User user = userService.getCurrentUser();
        return notificationRepository.getNotificationsBySendToUserStatusTrue(user.getId());
    }

    /**
     * get all notifications.
     *
     * @return list of notifications found.
     */
    @Override
    public List<Notifications> getAllNotificationsBySendToUserStatusFalse() {
        User user = userService.getCurrentUser();
        return notificationRepository.getNotificationsBySendToUserStatusFalse(user.getId());
    }


    /**
     * get number notifications.
     *
     * @return number of notifications user.
     */
    @Override
    public Long getNumberNotificationUser() {
        User user = userService.getCurrentUser();
        return notificationRepository.getNomberNoticationUser(user.getId());
    }


    /**
     * delete notification.
     *
     * @param notificationDTO the notificationDTO.
     */
    @Override
    public void delete(NotificationDTO notificationDTO) {

        Optional<Notifications> notification = notificationRepository.findById(notificationDTO.getId());

        notification.ifPresent(notificationRepository::delete);

    }

    /**
     * delete notification by Id.
     *
     * @param id the id.
     */
    @Override
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Page<Notifications> getAllNotificationSpecification(NotificationCriteriaDTO notificationCriteria, Pageable pageable) {
        return notificationRepository.findAll(NotificationSpecification.getSpecification(notificationCriteria), pageable);
    }
}
