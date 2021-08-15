package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.AgencyUser;
import cm.skysoft.app.repository.AgencyUserRepository;
import cm.skysoft.app.service.AgencyUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/23/21.
 */
@Service
public class AgencyUserServiceImpl implements AgencyUserService {


    private final AgencyUserRepository agencyUserRepository;

    public AgencyUserServiceImpl(AgencyUserRepository agencyUserRepository) {
        this.agencyUserRepository = agencyUserRepository;
    }

    /**
     * save agencyUser.
     *
     * @param agencyUser the agencyUser.
     * @return agencyUser save.
     */
    @Override
    public AgencyUser save(AgencyUser agencyUser) {
        return agencyUserRepository.save(agencyUser);
    }

    /**
     * update agencyUser.
     *
     * @param agencyUser the agencyUser.
     */
    @Override
    public void update(AgencyUser agencyUser) {
        agencyUserRepository.save(agencyUser);
    }

    /**
     * get agencyUser by id.
     *
     * @param id the id.
     * @return agencyUser found.
     */
    @Override
    public Optional<AgencyUser> getAgencyUserById(Long id) {
        return agencyUserRepository.findById(id);
    }

    /**
     * get agencyUser by id.
     *
     * @param idUser the idUser.
     * @param idAgency the idAgency.
     * @return agencyUser found.
     */
    @Override
    public Optional<AgencyUser> getAgencyUserByIdUserAndIdAgency(Long idUser, Long idAgency) {
        return agencyUserRepository.getAgencyUserByIdUserAndIdAgency(idUser, idAgency);
    }

    /**
     * get agencyUser by idUser.
     *
     * @param idUser and idAgency the idUser.
     * @return agencyUser found.
     */
    @Override
    public Optional<AgencyUser> getAgencyUserByIdUser(Long idUser) {
        return agencyUserRepository.getAgencyUserByIdUser(idUser);
    }

    /**
     * get all agencyUsers.
     *
     * @param pageable the pageable.
     * @return list of agencyUser found.
     */
    @Override
    public Page<AgencyUser> getAllAgencyUsers(Pageable pageable) {
        return agencyUserRepository.findAll(pageable);
    }

    /**
     * get all agencyUsers.
     *
     * @return list of agencyUser found.
     */
    @Override
    public List<AgencyUser> getAllAgencyUsers() {
        return agencyUserRepository.findAll();
    }

    /**
     * delete agencyUser.
     *
     * @param agencyUser the agencyUser.
     */
    @Override
    public void delete(AgencyUser agencyUser) {
        agencyUserRepository.delete(agencyUser);
    }

    /**
     * delete agencyUser by Id.
     *
     * @param id the id.
     */
    @Override
    public void deleteById(Long id) {
        agencyUserRepository.deleteById(id);
    }




}
