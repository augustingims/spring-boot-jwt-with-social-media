package cm.skysoft.app.repository;

import cm.skysoft.app.domain.AgencyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by francis on 2/23/21.
 */
public interface AgencyUserRepository extends JpaRepository<AgencyUser, Long>, JpaSpecificationExecutor<AgencyUser> {

    @Query("SELECT au FROM AgencyUser au WHERE au.agency.id = :idAgency and  au.user.id = :idUser")
    Optional<AgencyUser> getAgencyUserByIdUserAndIdAgency(@Param(value = "idUser") Long idUser,
                                                          @Param(value = "idAgency") Long idAgency);

    @Query("SELECT au FROM AgencyUser au WHERE au.user.id = :idUser")
    Optional<AgencyUser> getAgencyUserByIdUser(@Param(value = "idUser") Long idUser);
}
