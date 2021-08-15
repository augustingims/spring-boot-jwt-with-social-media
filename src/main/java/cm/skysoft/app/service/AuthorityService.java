package cm.skysoft.app.service;

import cm.skysoft.app.service.dto.AuthorityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorityService {

    /**
     * save authority.
     * @param authority the authority
     */
     void save(AuthorityDTO authority);

    /**
     * update authority.
     * @param authority the authority
     */
     void update(AuthorityDTO authority);

    /**
     * get authority by id.
     * @param id the id
     * @return authority found.
     */
    Optional<AuthorityDTO> getAuthorityById(String id);

    /**
     * get all authorities.
     * @param pageable the pageable
     * @return list of authorities found.
     */
    Page<AuthorityDTO> getAllAuthorities(Pageable pageable);

    /**
     * get all authorities.
     * @return list of authorities found.
     */
    List<AuthorityDTO> getAllAuthorities();


    /**
     * delete authority.
     * @param authority the authority
     */
     void delete(AuthorityDTO authority);

    /**
     * delete authority by Id.
     * @param id the id
     */
     void deleteById(String id);
}
