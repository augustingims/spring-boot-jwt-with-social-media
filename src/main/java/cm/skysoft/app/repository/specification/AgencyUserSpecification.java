package cm.skysoft.app.repository.specification;

import cm.skysoft.app.criteria.AgencyUserCriteria;
import cm.skysoft.app.domain.Agency;
import cm.skysoft.app.domain.AgencyUser;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.utils.FieldNameBD;
import cm.skysoft.app.utils.SecurityUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Objects;
import java.util.Optional;

public final class AgencyUserSpecification {

    public AgencyUserSpecification() {
    }

    private static Specification<AgencyUser> withAgency(Long idAgency) {
        return (Specification<AgencyUser>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<AgencyUser, Agency> join = root.join(FieldNameBD.FIELD_AGENCY);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_ID), idAgency);
        };
    }

    private static Specification<AgencyUser> withAgencyRegion(Long idAgencyRegion) {
        return (Specification<AgencyUser>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<AgencyUser, Agency> join = root.join(FieldNameBD.FIELD_AGENCY);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_AGENCY_REGION), idAgencyRegion);
        };
    }

    private static Specification<AgencyUser> withLogin(String login) {
        return (Specification<AgencyUser>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<AgencyUser, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_LOGIN), login);
        };
    }

    private static Specification<AgencyUser> withProfilUser(String profil) {
        return (Specification<AgencyUser>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<AgencyUser, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_AUTHORITY).get(FieldNameBD.FIELD_NAME), profil);
        };
    }

    private static Specification<AgencyUser> withCurrentUser(String currentUser) {
        return (Specification<AgencyUser>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<AgencyUser, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_LOGIN), currentUser);
        };
    }

    private static Specification<AgencyUser> withUserCode(String userCode) {
        return (Specification<AgencyUser>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<AgencyUser, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_USER_CODE), userCode);
        };
    }

    private static Specification<AgencyUser> withPositionName(String positionName) {
        return (Specification<AgencyUser>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<AgencyUser, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER);
            return criteriaBuilder.equal(join.get(FieldNameBD.FIELD_POSITION_NAME), positionName);
        };
    }

    private static Specification<AgencyUser> withNotCurrentUser(String currentUser){
        return (Specification<AgencyUser>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<AgencyUser, User> join = root.join(FieldNameBD.FIELD_JOIN_NAME_USER);
            return criteriaBuilder.not(join.get(FieldNameBD.FIELD_LOGIN).in(currentUser));
        };
    }

    public static Specification<AgencyUser> getSpecification(final AgencyUserCriteria agencyUserCriteria) {
        Specification<AgencyUser> specification = null;

        Optional<String> user = SecurityUtils.getCurrentUserLogin();

        if(agencyUserCriteria.getPositionName() != null){
            specification = Objects.requireNonNull(Specification.where(specification)).and(AgencyUserSpecification.withPositionName(agencyUserCriteria.getPositionName()));
        }
        if(agencyUserCriteria.getUserCode() != null){
            specification = Objects.requireNonNull(Specification.where(specification)).and(AgencyUserSpecification.withUserCode(agencyUserCriteria.getUserCode()));
        }
        if(agencyUserCriteria.getLogin() != null){
            specification = Objects.requireNonNull(Specification.where(specification)).and(AgencyUserSpecification.withLogin(agencyUserCriteria.getLogin()));
        }
        if(agencyUserCriteria.getIdAgency() != null){
            specification = Objects.requireNonNull(Specification.where(specification)).and(AgencyUserSpecification.withAgency(agencyUserCriteria.getIdAgency()));
        }
        if(agencyUserCriteria.getIdAgencyRegion() != null){
            specification = Objects.requireNonNull(Specification.where(specification)).and(AgencyUserSpecification.withAgencyRegion(agencyUserCriteria.getIdAgencyRegion()));
        }
        if(agencyUserCriteria.getProfil() != null){
            specification = Objects.requireNonNull(Specification.where(specification)).and(AgencyUserSpecification.withProfilUser(agencyUserCriteria.getProfil()));
        }
        if(user.isPresent()){
            specification = Objects.requireNonNull(Specification.where(specification)).and(AgencyUserSpecification.withNotCurrentUser(user.get()));
        }

        return specification;
    }
}
