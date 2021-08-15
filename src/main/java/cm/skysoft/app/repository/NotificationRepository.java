package cm.skysoft.app.repository;

import cm.skysoft.app.domain.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by francis on 2/25/21.
 */
public interface NotificationRepository extends JpaRepository<Notifications, Long>, JpaSpecificationExecutor<Notifications> {

    @Query("SELECT n FROM Notifications n WHERE n.sendTo.id = :idSentToUser ORDER BY createDate ASC")
    List<Notifications> getNotificationsBySendToUser(@Param(value = "idSentToUser") Long idSentToUser);

    @Query("SELECT COUNT(n) FROM Notifications n WHERE n.sendTo.id = :idSentToUser AND n.status = FALSE ")
    Long getNomberNoticationUser(@Param(value = "idSentToUser") Long idSentToUser);


    @Query("SELECT n FROM Notifications n WHERE n.sendTo.id = :idSentToUser AND n.status = FALSE")
    List<Notifications> getNotificationsBySendToUserStatusFalse(@Param(value = "idSentToUser") Long id);

    @Query("SELECT n FROM Notifications n WHERE n.sendTo.id = :idSentToUser AND n.status = TRUE")
    List<Notifications> getNotificationsBySendToUserStatusTrue(@Param(value = "idSentToUser") Long id);
}
