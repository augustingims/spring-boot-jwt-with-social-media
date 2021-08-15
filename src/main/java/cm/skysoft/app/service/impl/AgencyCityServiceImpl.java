package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.AgencyCity;
import cm.skysoft.app.dto.AgencyCityDTO;
import cm.skysoft.app.mapper.AgencyCityMapper;
import cm.skysoft.app.repository.AgencyCityRepository;
import cm.skysoft.app.service.AgencyCityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by daniel on 2/10/21.
 */
@Service
@Transactional
public class AgencyCityServiceImpl implements AgencyCityService {

    private final Logger logger = LoggerFactory.getLogger(AgencyCityServiceImpl.class);

    private final AgencyCityRepository agencyCityRepository;

    public AgencyCityServiceImpl(AgencyCityRepository agencyCityRepository) {
        this.agencyCityRepository = agencyCityRepository;
    }

    /**
     * save agencyCity.
     *
     * @param agencyCityDTO the agencyCityDTO.
     * @return agencyCity save.
     */
    @Transactional
    @Override
    public AgencyCity save(AgencyCityDTO agencyCityDTO) {
        logger.debug("REQ - save agencyCity");
        return agencyCityRepository.save(AgencyCityMapper.INSTANCE.toAgencyCity(agencyCityDTO));
    }

    /**
     * update agencyCity.
     *
     * @param agencyCityDTO the agencyCityDTO.
     * @return agencyCity found.
     */
    @Transactional
    @Override
    public void update(AgencyCityDTO agencyCityDTO) {
        logger.debug("REQ - update agencyCity");
        if(getAgencyCityById(agencyCityDTO.getId()).isPresent()){
            agencyCityRepository.save(AgencyCityMapper.INSTANCE.toAgencyCity(agencyCityDTO));
        }
    }

    /**
     * get agencyCity by id.
     *
     * @param id the id.
     * @return agencyCity found.
  /*   */
    @Transactional(readOnly = true)
    @Override
    public Optional<AgencyCityDTO> getAgencyCityById(Long id) {
        logger.debug("REQ - get agencyCity by id");
        return agencyCityRepository.findById(id).map(AgencyCityMapper.INSTANCE::toAgencyCityDto);
    }

    /**
     * get agencyCity by id.
     *
     * @param idAgencyCity the idAgencyCity
     * @return agencyCity found.
     */
    @Override
    public AgencyCity getAgencyCityByIdAgencyCity(Long idAgencyCity) {
        return agencyCityRepository.findByIdAgencyCity(idAgencyCity);
    }


    /**
     * get all agencies.
     *
     * @param pageable the pageable.
     * @return list of agencyCity found.
     */
    @Transactional(readOnly = true)
    @Override
    public Page<AgencyCityDTO> getAllagencyCities(Pageable pageable) {
        logger.debug("REQ - get all visit page");
        Page<AgencyCityDTO> agencyDTOs = agencyCityRepository.findAll(pageable)
                .map(AgencyCityMapper.INSTANCE::toAgencyCityDto);
        if(!agencyDTOs.isEmpty()){
            return agencyDTOs;
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    /**
     * get all agencyCities.
     *
     * @return list of agencyCity found.
     */
    @Transactional(readOnly = true)
    @Override
    public List<AgencyCityDTO> getAllagencyCities() {
        logger.debug("REQ - get all visit");
        List<AgencyCityDTO> agencyDTOs = agencyCityRepository.findAll()
                .stream()
                .map(AgencyCityMapper.INSTANCE::toAgencyCityDto)
                .collect(Collectors.toList());
        if (!agencyDTOs.isEmpty()) {
            return agencyDTOs;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * delete agencyCity.
     *
     * @param agencyCity the agencyCity
     */
    @Transactional
    @Override
    public void delete(AgencyCityDTO agencyCity) {
        logger.debug("REQ - delete agencyCity");
        agencyCityRepository.delete(AgencyCityMapper.INSTANCE.toAgencyCity(agencyCity));
    }

    /**
     * delete agencyCity by Id.
     *
     * @param id the id.
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        logger.debug("REQ - delete agencyCity by id");
        agencyCityRepository.deleteById(id);
    }
}
