package cm.skysoft.app.repository.specification;

import cm.skysoft.app.criteria.UserCriteriaDTO;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.utils.FieldNameBD;
import cm.skysoft.app.utils.SecurityUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.Optional;

public final class UserSpecification {

    public UserSpecification() {
    }

    private static Specification<User> withPositionName(String positionName) {
        return (Specification<User>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_POSITION_NAME), positionName);
    }

    private static Specification<User> withUserCode(String userCode) {
        return (Specification<User>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_USER_CODE), userCode);
    }

    private static Specification<User> withLogin(String login) {
        return (Specification<User>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(FieldNameBD.FIELD_LOGIN), login);
    }

    private static Specification<User> withNotCurrentUser(String currentUser){
        return (Specification<User>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.not(root.get(FieldNameBD.FIELD_LOGIN).in(currentUser));
    }

    public static Specification<User> getSpecification(final UserCriteriaDTO userCriteriaDTO){
        Specification<User> specification = null;

        Optional<String> user = SecurityUtils.getCurrentUserLogin();

        if(userCriteriaDTO.getPositionName() != null){
            specification = Objects.requireNonNull(Specification.where(specification)).and(UserSpecification.withPositionName(userCriteriaDTO.getPositionName()));
        }
        if(userCriteriaDTO.getUserCode() != null){
            specification = Objects.requireNonNull(Specification.where(specification)).and(UserSpecification.withUserCode(userCriteriaDTO.getUserCode()));
        }
        if(userCriteriaDTO.getLogin() != null){
            specification = Objects.requireNonNull(Specification.where(specification)).and(UserSpecification.withLogin(userCriteriaDTO.getLogin()));
        }

        if(user.isPresent()){
            specification = Objects.requireNonNull(Specification.where(specification)).and(UserSpecification.withNotCurrentUser(user.get()));
        }

        return specification;
    }
}
