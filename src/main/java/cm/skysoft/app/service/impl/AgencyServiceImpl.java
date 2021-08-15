package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.Agency;
import cm.skysoft.app.dto.AgencyDTO;
import cm.skysoft.app.repository.AgencyRepository;
import cm.skysoft.app.service.AgencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/12/21.
 */
@Service
public class AgencyServiceImpl implements AgencyService {

    private final Logger LOGGER = LoggerFactory.getLogger(AgencyServiceImpl.class);

    private final AgencyRepository agencyRepository;

    public AgencyServiceImpl(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    /**
     * save agency.
     *
     * @param agency the agency.
     * @return agency found.
     */
    @Transactional
    @Override
    public Agency save(Agency agency) {
        LOGGER.debug("REQ - save agency");
        return agencyRepository.save(agency);
    }

    /**
     * update agency.
     *
     * @param agency the agency.
     */
    @Transactional
    @Override
    public void update(AgencyDTO agency) {
       /* LOGGER.debug("REQ - update agency");
        if(agency!=null){
            if(getAgencyById(agency.getId()).isPresent()){
                agencyRepository.save(AgencyMapper.INSTANCE.toAgency(agency));
            }
        }*/
    }

    /**
     * get agency by id.
     *
     * @param id the id.
     * @return agency found.
  /*   *//*
    @Transactional(readOnly = true)
    @Override
    public Optional<AgencyDTO> getAgencyById(Long id) {
        LOGGER.debug("REQ - get agency by id");
        return agencyRepository.findById(id).map(AgencyMapper.INSTANCE::toAgencyDTO);
    }*/

    /**
     * get agency by id.
     *
     * @param idAgency the idAgency
     * @return agency found.
     */
    @Override
    public Agency getAgencyByIdAgency(Long idAgency) {
        return agencyRepository.findByIdAgency(idAgency);
    }

    @Override
    public Optional<Agency> getAgencyById(Long idAgency) {
        return agencyRepository.findById(idAgency);
    }

    @Override
    public List<Agency> findAll() {
        return agencyRepository.findAllByOrderByName();
    }

    /**
     * get all agencies.
     *
     * @param pageable the pageable.
     * @return list of agency found.
     */
 /*   @Transactional(readOnly = true)
    @Override
    public Page<AgencyDTO> getAllAgencies(Pageable pageable) {
        LOGGER.debug("REQ - get all visit page");
        Page<AgencyDTO> agencyDTOs = agencyRepository.findAll(pageable)
                .map(AgencyMapper.INSTANCE::toAgencyDTO);
        if(!agencyDTOs.isEmpty()){
            return agencyDTOs;
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }*/

    /**
     * get all agencies.
     *
     * @return list of agency found.
     */
   /* @Transactional(readOnly = true)
    @Override
    public List<AgencyDTO> getAllAgencies() {
        LOGGER.debug("REQ - get all visit");
        List<AgencyDTO> agencyDTOs = agencyRepository.findAll()
                .stream()
                .map(AgencyMapper.INSTANCE::toAgencyDTO)
                .collect(Collectors.toList());
        if (!agencyDTOs.isEmpty()) {
            return agencyDTOs;
        } else {
            return new ArrayList<>();
        }
    }*/

    /**
     * delete agency.
     *
     * @param agency the agency
     */
    @Transactional
    @Override
    public void delete(AgencyDTO agency) {
       /* LOGGER.debug("REQ - delete agency");
        agencyRepository.delete(AgencyMapper.INSTANCE.toAgency(agency));*/
    }

    /**
     * delete agency by Id.
     *
     * @param id the id.
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        LOGGER.debug("REQ - delete agency by id");
        agencyRepository.deleteById(id);
    }
}
