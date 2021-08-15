package cm.skysoft.app.repository.specification;

import cm.skysoft.app.criteria.SuggestionCriteriaDTO;
import cm.skysoft.app.domain.Clients;
import cm.skysoft.app.domain.Suggestions;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.utils.FieldNameBD;
import cm.skysoft.app.utils.MethoUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.Objects;

public final class SuggestionSpecification {

    public SuggestionSpecification() {
    }

    private static Specification<Suggestions> withCustomer(Long idCustomer){
        return (Specification<Suggestions>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Suggestions, Clients> join = root.join(FieldNameBD.FIELD_JOIN_NAME_CUSTOMER_SUGGESTION);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_NAME_CUSTOMER_ID_CLIENT), idCustomer);
        };
    }

    private static Specification<Suggestions> withDateBefore(String dateBefore){
        return (Specification<Suggestions>) (root, criteriaQuery, criteriaBuilder) -> {

            LocalDateTime dateTimeBefore = MethoUtils.convertorDateWithoutFormat(dateBefore);

            return criteriaBuilder.lessThanOrEqualTo(root.get(FieldNameBD.FIELD_NAME_SUGGESTION_DATE_CREATE), dateTimeBefore);
        };
    }

    private static Specification<Suggestions> withDateAfter(String dateAfter){
        return (Specification<Suggestions>) (root, criteriaQuery, criteriaBuilder) -> {

            LocalDateTime dateTimeAfter = MethoUtils.convertorDateWithoutFormat(dateAfter);

            return criteriaBuilder.greaterThanOrEqualTo(root.get(FieldNameBD.FIELD_NAME_SUGGESTION_DATE_CREATE), dateTimeAfter);
        };
    }

    private static Specification<Suggestions> withStatus(Boolean status){
        return (Specification<Suggestions>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_STATUS), status);
    }

    private static Specification<Suggestions> withValidated(Boolean validated){
        return (Specification<Suggestions>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_VALIDATED), validated);
    }

    private static Specification<Suggestions> withNotValidated(Boolean notValidated){
        return (Specification<Suggestions>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_NOT_VALIDATED), notValidated);
    }

    private static Specification<Suggestions> withUserSender(Long idUserSender){
        return (Specification<Suggestions>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Suggestions, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER_AFB_SENDER);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_NAME_USER_ID), idUserSender);
        };
    }

    private static Specification<Suggestions> withUserAfb(Long idUserAfb){
        return (Specification<Suggestions>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Suggestions, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER_AFB);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_NAME_USER_ID), idUserAfb);
        };
    }

    public static Specification<Suggestions> getSpecification(final SuggestionCriteriaDTO suggestionCriteria) {
        Specification<Suggestions> spec = null;
        if(suggestionCriteria.getStatus() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(SuggestionSpecification.withStatus(suggestionCriteria.getStatus()));
        }
        if(suggestionCriteria.getValidated() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(SuggestionSpecification.withValidated(suggestionCriteria.getValidated()));
        }
        if(suggestionCriteria.getNotValidated() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(SuggestionSpecification.withNotValidated(suggestionCriteria.getNotValidated()));
        }
        if(suggestionCriteria.getDateBefore() != null && !suggestionCriteria.getDateBefore().isEmpty()){
            spec = Objects.requireNonNull(Specification.where(spec)).and(SuggestionSpecification.withDateBefore(suggestionCriteria.getDateBefore()));
        }
        if(suggestionCriteria.getDateAfter() != null && !suggestionCriteria.getDateAfter().isEmpty()){
            spec = Objects.requireNonNull(Specification.where(spec)).and(SuggestionSpecification.withDateAfter(suggestionCriteria.getDateAfter()));
        }
        if(suggestionCriteria.getIdCustomer() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(SuggestionSpecification.withCustomer(suggestionCriteria.getIdCustomer()));
        }
        if(suggestionCriteria.getIdUserSender() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(SuggestionSpecification.withUserSender(suggestionCriteria.getIdUserSender()));
        }
        if(suggestionCriteria.getIdUserAfb() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(SuggestionSpecification.withUserAfb(suggestionCriteria.getIdUserAfb()));
        }

        return spec;
    }
}
