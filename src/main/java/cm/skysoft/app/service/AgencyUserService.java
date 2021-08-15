package cm.skysoft.app.service;

import cm.skysoft.app.domain.AgencyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/23/21.
 */
public interface AgencyUserService {


    /**
     * save agencyUser.
     * @param agencyUser the agencyUser.
     * @return agencyUser save.
     */
    AgencyUser save(AgencyUser agencyUser);

    /**
     * update agencyUser.
     * @param agencyUser the agencyUser.
     */
    void update(AgencyUser agencyUser);

    /**
     * get agencyUser by id.
     * @param id the id.
     * @return agencyUser found.
     */
    Optional<AgencyUser> getAgencyUserById(Long id);

    /**
     * get agencyUser by id.
     * @param idUser and idAgency the id.
     * @return agencyUser found.
     */
    Optional<AgencyUser> getAgencyUserByIdUserAndIdAgency(Long idUser, Long idAgency);

    /**
     * get agencyUser by id.
     * @param idUser and idAgency the id.
     * @return agencyUser found.
     */
    Optional<AgencyUser> getAgencyUserByIdUser(Long idUser);

    /**
     * get all agencyUsers.
     * @param pageable the pageable.
     * @return list of agencyUser found.
     */
    Page<AgencyUser> getAllAgencyUsers(Pageable pageable);

    /**
     * get all agencyUsers.
     * @return list of agencyUser found.
     */
    List<AgencyUser> getAllAgencyUsers();

    /**
     * delete agencyUser.
     * @param agencyUser the agencyUser.
     */
    void delete(AgencyUser agencyUser);

    /**
     * delete agencyUser by Id.
     * @param id the id.
     */
    void deleteById(Long id);
}
