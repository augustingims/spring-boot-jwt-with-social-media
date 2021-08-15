package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.AgencyRegion;
import cm.skysoft.app.dto.AgencyRegionDTO;
import cm.skysoft.app.mapper.AgencyRegionMapper;
import cm.skysoft.app.repository.AgencyRegionRepository;
import cm.skysoft.app.service.AgencyRegionService;
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
public class AgencyRegionServiceImpl implements AgencyRegionService {

    private final Logger LOGGER = LoggerFactory.getLogger(ClientsServiceImpl.class);

    private final AgencyRegionRepository agencyRegionRepository;

    public AgencyRegionServiceImpl(AgencyRegionRepository agencyRegionRepository) {
        this.agencyRegionRepository = agencyRegionRepository;
    }

    /**
     * save client.
     *
     * @param agencyRegion the client.
     * @return agencyRegion save.
     */
    @Transactional
    @Override
    public AgencyRegion save(AgencyRegionDTO agencyRegion) {
        LOGGER.debug("REQ - save agency");
        return agencyRegionRepository.save(AgencyRegionMapper.INSTANCE.toAgencyRegion(agencyRegion));
    }
    /**
     * update agencyRegion.
     *
     * @param agencyRegion the agencyRegion.
     * @return agencyRegion found.
     */
    @Transactional
    @Override
    public void update(AgencyRegionDTO agencyRegion) {
        LOGGER.debug("REQ - update agencyRegion");
        if(agencyRegion!=null){
            if(getAgencyRegionById(agencyRegion.getId()).isPresent()){
                agencyRegionRepository.save(AgencyRegionMapper.INSTANCE.toAgencyRegion(agencyRegion));
            }
        }
    }

    /**
     * get agencyRegion by id.
     *
     * @param id the id.
     * @return agencyRegion found.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<AgencyRegionDTO> getAgencyRegionById(Long id) {
        LOGGER.debug("REQ - get agencyRegion by id");
        return agencyRegionRepository.findById(id).map(AgencyRegionMapper.INSTANCE::toAgencyRegionDTO);
    }

    /**
     * get agencyRegion by idAgencyRegion.
     *
     * @param idAgencyRegion the id
     * @return agencyRegion found.
     */
    @Override
    public AgencyRegion getAgencyRegionByIdAgencyRegion(Long idAgencyRegion) {
        return agencyRegionRepository.findByIdAgencyRegion(idAgencyRegion);
    }


    /**
     * get all agencyRegions.
     *
     * @param pageable the pageable.
     * @return list of agencyRegion found.
     */
    @Transactional(readOnly = true)
    @Override
    public Page<AgencyRegionDTO> getAllAgencyRegions(Pageable pageable) {
        LOGGER.debug("REQ - get all agencyRegion page");
        Page<AgencyRegionDTO> agencyRegionDTOs = agencyRegionRepository.findAll(pageable)
                .map(AgencyRegionMapper.INSTANCE::toAgencyRegionDTO);
        if(!agencyRegionDTOs.isEmpty()){
            return agencyRegionDTOs;
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }
    /**
     * get all agencyRegion.
     *
     * @return list of agencyRegion found.
     */
    @Transactional(readOnly = true)
    @Override
    public List<AgencyRegionDTO> getAllAgencyRegions() {
        LOGGER.debug("REQ - get all agencyRegion");
        List<AgencyRegionDTO> agencyRegionDTOs = agencyRegionRepository.findAllByOrderByAgencyRegionName()
                .stream()
                .map(AgencyRegionMapper.INSTANCE::toAgencyRegionDTO)
                .collect(Collectors.toList());
        if (!agencyRegionDTOs.isEmpty()) {
            return agencyRegionDTOs;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * delete agencyRegion.
     *
     * @param agencyRegion the agencyRegion
     */
    @Transactional
    @Override
    public void delete(AgencyRegionDTO agencyRegion) {
        LOGGER.debug("REQ - delete agencyRegion");
        agencyRegionRepository.delete(AgencyRegionMapper.INSTANCE.toAgencyRegion(agencyRegion));
    }

    /**
     * delete agencyRegion by Id.
     *
     * @param id the id.
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        LOGGER.debug("REQ - delete agencyRegion by id");
        agencyRegionRepository.deleteById(id);
    }

}
