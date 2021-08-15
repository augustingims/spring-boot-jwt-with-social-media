package cm.skysoft.app.repository.specification;

import cm.skysoft.app.criteria.NotificationCriteriaDTO;
import cm.skysoft.app.domain.Notifications;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.utils.FieldNameBD;
import cm.skysoft.app.utils.MethoUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.Objects;

public final class NotificationSpecification {

    public NotificationSpecification() {
    }

    private static Specification<Notifications> withStatus(Boolean status){
        return (Specification<Notifications>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_STATUS), status);
    }

    private static Specification<Notifications> withTypeNotification(String typeNotification){
        return (Specification<Notifications>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_TYPE_NOTIFICATION), typeNotification);
    }

    private static Specification<Notifications> withUserReceived(Long idUserAfb){
        return (Specification<Notifications>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Notifications, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER_AFB_NOTIFICATION);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_NAME_USER_ID), idUserAfb);
        };
    }

    private static Specification<Notifications> withDateBefore(String dateBefore){
        return (Specification<Notifications>) (root, criteriaQuery, criteriaBuilder) -> {

            LocalDateTime dateTimeBefore = MethoUtils.convertorDateWithoutFormat(dateBefore);

            return criteriaBuilder.lessThanOrEqualTo(root.get(FieldNameBD.FIELD_NAME_NOTIFICATION_DATE_CREATE), dateTimeBefore);
        };
    }

    private static Specification<Notifications> withDateAfter(String dateAfter){
        return (Specification<Notifications>) (root, criteriaQuery, criteriaBuilder) -> {

            LocalDateTime dateTimeAfter = MethoUtils.convertorDateWithoutFormat(dateAfter);

            return criteriaBuilder.greaterThanOrEqualTo(root.get(FieldNameBD.FIELD_NAME_NOTIFICATION_DATE_CREATE), dateTimeAfter);
        };
    }

    public static Specification<Notifications> getSpecification(final NotificationCriteriaDTO notificationCriteria) {
        Specification<Notifications> spec = null;
        if(notificationCriteria.getStatus() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(NotificationSpecification.withStatus(notificationCriteria.getStatus()));
        }
        if(notificationCriteria.getDateBefore() != null && !notificationCriteria.getDateBefore().isEmpty()){
            spec = Objects.requireNonNull(Specification.where(spec)).and(NotificationSpecification.withDateBefore(notificationCriteria.getDateBefore()));
        }
        if(notificationCriteria.getDateAfter() != null && !notificationCriteria.getDateAfter().isEmpty()){
            spec = Objects.requireNonNull(Specification.where(spec)).and(NotificationSpecification.withDateAfter(notificationCriteria.getDateAfter()));
        }

        if(notificationCriteria.getIdUserReceived() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(NotificationSpecification.withUserReceived(notificationCriteria.getIdUserReceived()));
        }

        return spec;
    }

}
