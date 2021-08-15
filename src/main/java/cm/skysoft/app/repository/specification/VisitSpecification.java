package cm.skysoft.app.repository.specification;

import cm.skysoft.app.criteria.VisitCriteriaDTO;
import cm.skysoft.app.domain.Clients;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.domain.Visits;
import cm.skysoft.app.utils.FieldNameBD;
import cm.skysoft.app.utils.MethoUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.Objects;

public final class VisitSpecification {

    public VisitSpecification() {
    }

    private static Specification<Visits> withTypeVisit(String visitType) {
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_VISIT_TYPE), visitType);
    }

    private static Specification<Visits> withMeansUsed(String meansUsed) {
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_MEANS_USED), meansUsed);
    }

    private static Specification<Visits> withExecutionFalse(boolean execution) {
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_VISITE_EXECUTION), execution);
    }

    private static Specification<Visits> withPreparationFalse(boolean preparation) {
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_VISITE_PREPARATION), preparation);
    }

    private static Specification<Visits> withReportingFalse(boolean reporting) {
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_VISITE_REPORTING), reporting);
    }

    private static Specification<Visits> withArchivateFalse(boolean archivate) {
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_VISITE_ARCHIVATE), archivate);
    }

    private static Specification<Visits> withPlanificationFalse(boolean planification) {
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_NAME_VISITE_PLANIFICATION), planification);
    }

    public static Specification<Visits> withCustomer(Long idCustomer){
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Visits, Clients> join = root.join(FieldNameBD.FIELD_JOIN_NAME_CUSTOMER);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_NAME_CUSTOMER_ID_CLIENT), idCustomer);
        };
    }

    private static Specification<Visits> withCodeCustomer(String codeCustomer){
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Visits, Clients> join = root.join(FieldNameBD.FIELD_JOIN_NAME_CUSTOMER);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_NAME_CUSTOMER_CODE_CLIENT), codeCustomer);
        };
    }

    @Transient
    private static Specification<Visits> withUserAfb(Long idUserAfb){
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<Visits, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER_AFB);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_NAME_USER_ID), idUserAfb);
        };
    }

    private static Specification<Visits> withDateBefore(String dateBefore){
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> {

            LocalDateTime dateTimeBefore = MethoUtils.convertorDateWithoutFormat(dateBefore);

            return criteriaBuilder.lessThanOrEqualTo(root.get(FieldNameBD.FIELD_NAME_VISIT_DATE), dateTimeBefore);
        };
    }

    private static Specification<Visits> withDateAfter(String dateAfter){
        return (Specification<Visits>) (root, criteriaQuery, criteriaBuilder) -> {

            LocalDateTime dateTimeAfter = MethoUtils.convertorDateWithoutFormat(dateAfter);

            return criteriaBuilder.greaterThanOrEqualTo(root.get(FieldNameBD.FIELD_NAME_VISIT_DATE), dateTimeAfter);
        };
    }

    public static Specification<Visits> getSpecification(final VisitCriteriaDTO visitCriterai) {
        Specification<Visits> spec = null;
        if(visitCriterai.getVisitType() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withTypeVisit(visitCriterai.getVisitType()));
        }
        if(visitCriterai.getMeansUsed() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withMeansUsed(visitCriterai.getMeansUsed()));
        }
        if(visitCriterai.getIdCustomer() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withCustomer(visitCriterai.getIdCustomer()));
        }
        if(visitCriterai.getCodeClient() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withCodeCustomer(visitCriterai.getCodeClient()));
        }
        if(visitCriterai.getDateBefore() != null && !visitCriterai.getDateBefore().isEmpty()){
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withDateBefore(visitCriterai.getDateBefore()));
        }
        if(visitCriterai.getDateAfter() != null && !visitCriterai.getDateAfter().isEmpty()){
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withDateAfter(visitCriterai.getDateAfter()));
        }
        if(visitCriterai.getIdUser() != null){
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withUserAfb(visitCriterai.getIdUser()));
        }
        if(visitCriterai.getExecution() != null) {
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withExecutionFalse(visitCriterai.getExecution()));
        }
        if(visitCriterai.getPreparation() != null) {
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withPreparationFalse(visitCriterai.getPreparation()));
        }
        if(visitCriterai.getReporting() != null) {
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withReportingFalse(visitCriterai.getReporting()));
        }
        if(visitCriterai.getArchivage() != null) {
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withArchivateFalse(visitCriterai.getArchivage()));
        }
        if(visitCriterai.getPlanification() != null) {
            spec = Objects.requireNonNull(Specification.where(spec)).and(VisitSpecification.withPlanificationFalse(visitCriterai.getPlanification()));
        }
        return spec;
    }
}
