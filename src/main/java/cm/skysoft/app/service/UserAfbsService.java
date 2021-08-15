package cm.skysoft.app.service;

import cm.skysoft.app.domain.User;
import cm.skysoft.app.domain.UserAfbs;
import cm.skysoft.app.dto.UserAfbDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/12/21.
 */
public interface UserAfbsService {
    /**
     * save userAfb.
     * @param userAfb the userAfb
     * @return list of userAfb found.
     */
    UserAfbs save(UserAfbs userAfb);

    /**
     * update userAfb.
     * @param userAfb the userAfb
     */
    void update(UserAfbDTO userAfb);

    /**
     * get userAfb by id.
     * @param id the id
     * @return userAfb found.
     */
    Optional<UserAfbDTO> getUserAfbById(Long id);

    /**
     * get userAfb by id.
     * @param idUserAfb the idUserAfb
     * @return userAfb found.
     */
    User getUserAfbByIdUserAfb(Long idUserAfb);

    /**
     * get user by id.
     * @param idUser the idUser
     * @return user found.
     */
    User getUserById(String idUser);

    /**
     * get all userAfb.
     * @param pageable the pageable
     * @return list of userAfb found.
     */
    Page<UserAfbDTO> getUserAfbById(Pageable pageable);

    /**
     * get all userAfbs.
     * @return list of userAfb found.
     */
    List<UserAfbDTO> getAllUserAfbs();

    /**
     * delete userAfb.
     * @param userAfb the userAfb
     */
    void delete(UserAfbDTO userAfb);

    /**
     * delete userAfb by Id.
     * @param id the id
     */
    void deleteById(Long id);
}
