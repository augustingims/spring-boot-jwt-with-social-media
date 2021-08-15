package cm.skysoft.app.web;

import cm.skysoft.app.criteria.NotificationCriteriaDTO;
import cm.skysoft.app.domain.Notifications;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.dto.NotificationDTO;
import cm.skysoft.app.service.NotificationsService;
import cm.skysoft.app.service.UserService;
import cm.skysoft.app.utils.HeaderUtils;
import cm.skysoft.app.utils.PaginationUtils;
import cm.skysoft.app.utils.SecurityUtils;
import cm.skysoft.app.web.exception.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by francis on 2/25/21.
 */
@RestController
@RequestMapping("/api")
public class NotificationsResource {
    
    @Value("${application.clientApp.name}")
    private String applicationName;

    private final NotificationsService notificationsService;

    private final UserService userService;

    public NotificationsResource(NotificationsService notificationsService, UserService userService) {
        this.notificationsService = notificationsService;
        this.userService = userService;
    }

    /**
     * POST  /notifications : save the notification.
     *
     * @param notification the visit View Model.
     *
     */
    @PostMapping("/notifications")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Notifications> saveNotification(@RequestBody @Valid NotificationDTO notification) {

        if (notification.getId()!=null)  {
            throw new BadRequestAlertException("The notification is already exists", "notifications", "idexists");
        }else {
            Notifications notifications = notificationsService.save(notification);
            return ResponseEntity.ok().body(notifications);
        }
    }

    /**
     * PUT  /notifications : update the notification.
     *
     * @param notification the notification View Model.
     *
     */
    @PutMapping("exit/notifications")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Notifications> updateNotification(@Valid @RequestBody NotificationDTO notification) {
        if (notification!= null && notification.getId()!=null)  {

            Notifications notificationUpdate = notificationsService.update(notification);
            return ResponseEntity.ok().body(notificationUpdate);
        }else {
            return ResponseEntity.ok().body(new Notifications());
        }
    }

    @GetMapping("/update-notification/{idNotification}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity updateSuggestion(@PathVariable Long idNotification) {
        notificationsService.updateNotification(idNotification);
        return ResponseEntity.ok().body(idNotification);
    }

    /**
     * GET  /notifications : get all the notifications.
     * @param pageable : the pageable.
     * @return list of notifications.
     */
    @GetMapping("/notifications")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Notifications>> getAllNotificationsPage(Pageable pageable) {
        final Page<Notifications> page = notificationsService.getAllNotifications(pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notificationsAll : get all the notifications.
     * @return list of notifications
     */
    @GetMapping("/notificationsAll")
    public ResponseEntity<List<Notifications>> getAllNotifications(@RequestParam(value = "status", required = false) String etat,
                                                                   @RequestParam(value = "idUser", required = false) Long idUser,
                                                                   @RequestParam(value = "resultmax", required = false) Long resultmax,
                                                                    @RequestParam(value = "dateDebut", required = false) LocalDateTime dateDebut,
                                                                   @RequestParam(value = "dateFin", required = false) LocalDateTime dateFin) {

        User user = new User();

        Boolean status = ("Tous".equals(etat) || etat == null) ? null
                : ("1".equals(etat) ? Boolean.TRUE : Boolean.FALSE);

        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        if(login != null) {
            user = userService.getUserWithAuthoritiesByLogin(login).orElse(new User());
        }

        final List<Notifications> notifications = notificationsService.getAllNotifications(status, idUser, user.getId(), resultmax, dateDebut, dateFin);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    /**
     * GET  /notificationsAll : get all the notifications.
     * @return list of notifications
     */
    @GetMapping("/getAllNotificationsSpecification")
    public ResponseEntity<List<Notifications>> getAllNotificationsSpecification(@RequestParam(value = "status", required = false) String etat,
                                                                    @RequestParam(value = "dateDebut", required = false) String dateDebut,
                                                                   @RequestParam(value = "dateFin", required = false) String dateFin,
                                                                                Pageable pageable) {

        NotificationCriteriaDTO notificationCriteria = new NotificationCriteriaDTO();

        Boolean status = ("Tous".equals(etat) || etat == null) ? null
                : ("1".equals(etat) ? Boolean.TRUE : Boolean.FALSE);

        User user = userService.getCurrentUser();

        notificationCriteria.setStatus(status);
        notificationCriteria.setDateAfter(dateDebut);
        notificationCriteria.setDateBefore(dateFin);
        notificationCriteria.setIdUserReceived(user.getId());

        Page<Notifications> notificationsPagePage = notificationsService.getAllNotificationSpecification(notificationCriteria, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),notificationsPagePage);
        return new ResponseEntity<>(notificationsPagePage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notificationsAllBySendTo : get all the notifications.
     * @return list of notifications
     */
    @GetMapping("/notificationsAllBySendTo")
    public ResponseEntity<List<Notifications>> getAllNotificationsBySendToUser() {
        final List<Notifications> notifications = notificationsService.getAllNotificationsBySendToUser();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    /**
     * GET  /notificationsAllBySendTo : get all the notifications.
     * @return list of notifications
     */
    @GetMapping("/notificationsAllBySendToAndStatusTrue")
    public List<Notifications> getAllNotificationsBySendToUserStatusTrue() {
        return notificationsService.getAllNotificationsBySendToUserStatusTrue();
    }

    /**
     * GET  /notificationsAllBySendTo : get all the notifications.
     * @return list of notification
     */
    @GetMapping("/notificationsAllBySendToAndStatusFalse")
    public List<Notifications> getAllNotificationsBySendToUserStatusFalse() {
        return notificationsService.getAllNotificationsBySendToUserStatusFalse();
    }

    /**
     * GET  /notificationsAllBySendTo : get all the notifications.
     * @return list of notifications
     */
    @GetMapping("/numberNotificationsUser")
    public ResponseEntity<Long> getNumberNotificationUser() {
        Long numberNotification = notificationsService.getNumberNotificationUser();

        if (numberNotification == null){
            numberNotification = (long) 0;
        }
        return new ResponseEntity<>(numberNotification, HttpStatus.OK);
    }

    @GetMapping("/notifications/{id}")
    public Notifications getNotificationById(@PathVariable Long id) {
        return notificationsService.getNotificationById(id).orElse(null);
    }

    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteVisitById(@PathVariable Long id) {
        notificationsService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert(applicationName, "notification.deleted", null)).build();
    }

    @DeleteMapping("/notifications")
    public ResponseEntity<Void> deleteVisit(@RequestBody NotificationDTO notification) {
        notificationsService.delete(notification);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName,"notification.deleted", null)).build();
    }
}
