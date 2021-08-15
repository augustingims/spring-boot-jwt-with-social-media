package cm.skysoft.app.service;

import cm.skysoft.app.criteria.NotificationCriteriaDTO;
import cm.skysoft.app.domain.Notifications;
import cm.skysoft.app.dto.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/25/21.
 */
public interface NotificationsService {

    /**
     * save Notifications.
     * @param notification the notification
     */
    Notifications save(NotificationDTO notification);

    /**
     * update notification.
     * @param notification the notification.
     */
    Notifications update(NotificationDTO notification);

    /**
     * update notification.
     * @param idNotification the notification
     */
    void updateNotification(Long idNotification);

    /**
     * get suggestion by id.
     * @param id the id
     * @return notification found.
     */
    Optional<Notifications> getNotificationById(Long id);

    /**
     * get all Notifications.
     * @param pageable the pageable.
     * @return list of notification found.
     */
    Page<Notifications> getAllNotifications(Pageable pageable);

    /**
     * get all notifications.
     * @return list of notifications found.
     */
    List<Notifications> getAllNotifications(Boolean status, Long idUser, Long sentTo, Long resultmax, LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * get all notifications.
     * @return list of notifications found.
     */
    List<Notifications> getAllNotificationsBySendToUser();

    /**
     * get all notifications.
     * @return list of notifications found.
     */
    List<Notifications> getAllNotificationsBySendToUserStatusTrue();

    /**
     * get all notifications.
     * @return list of notifications found.
     */
    List<Notifications> getAllNotificationsBySendToUserStatusFalse();

    /**
     * get number notifications.
     * @return number of notifications user.
     */
    Long getNumberNotificationUser();

    /**
     * delete notification.
     * @param notification the notification.
     */
    void delete(NotificationDTO notification);

    /**
     * delete notification by Id.
     * @param id the id.
     */
    void deleteById(Long id);

    /**
     * get all notifications with specification
     * @param notificationCriteria of the notificationCriteria
     * @param pageable of the pagination
     * @return list notifications
     */
    Page<Notifications> getAllNotificationSpecification(NotificationCriteriaDTO notificationCriteria, Pageable pageable);
}
