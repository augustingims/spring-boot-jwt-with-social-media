package cm.skysoft.app.repository.specification;

import cm.skysoft.app.criteria.LogsCriteria;
import cm.skysoft.app.domain.Logs;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.utils.FieldNameBD;
import cm.skysoft.app.utils.MethoUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.Objects;

public final class LogsSpecification {

    public LogsSpecification() {
    }

    private static Specification<Logs> withUserByLogin(String login) {
        return (Specification<Logs>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Logs, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_LOGIN), login);
        };
    }

    private static Specification<Logs> withDateBefore(String dateBefore){
        return (Specification<Logs>) (root, criteriaQuery, criteriaBuilder) -> {

            LocalDateTime dateTimeBefore = MethoUtils.convertorDateWithoutFormat(dateBefore);

            return criteriaBuilder.greaterThanOrEqualTo(root.get(FieldNameBD.FIELD_NAME_LOGS_DATE_OPERATION), dateTimeBefore);
        };
    }

    private static Specification<Logs> withDateAfter(String dateAfter){
        return (Specification<Logs>) (root, criteriaQuery, criteriaBuilder) -> {

            LocalDateTime dateTimeAfter = MethoUtils.convertorDateWithoutFormat(dateAfter);

            return criteriaBuilder.lessThanOrEqualTo(root.get(FieldNameBD.FIELD_NAME_LOGS_DATE_OPERATION), dateTimeAfter);
        };
    }

    public static  Specification<Logs> getSpecification(final LogsCriteria logsCriteria) {
        Specification<Logs> spec = null;

        if(logsCriteria.getLogin() != null) {
            spec = Objects.requireNonNull(Specification.where(spec)).and(LogsSpecification.withUserByLogin(logsCriteria.getLogin()));
        }
        if(logsCriteria.getDateBefore() != null) {
            spec = Objects.requireNonNull(Specification.where(spec)).and(LogsSpecification.withDateBefore(logsCriteria.getDateBefore()));
        }
        if(logsCriteria.getDateAfter() != null) {
            spec = Objects.requireNonNull(Specification.where(spec)).and(LogsSpecification.withDateAfter(logsCriteria.getDateAfter()));
        }

        return  spec;
    }
}
