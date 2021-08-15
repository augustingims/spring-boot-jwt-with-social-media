package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.AgencyCountry;
import cm.skysoft.app.dto.AgencyCountryDTO;
import cm.skysoft.app.mapper.AgencyCountryMapper;
import cm.skysoft.app.repository.AgencyCountryRepository;
import cm.skysoft.app.service.AgencyCountryService;
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
public class AgencyCountyServiceImpl implements AgencyCountryService {

    private final Logger logger = LoggerFactory.getLogger(AgencyCountyServiceImpl.class);

    private final AgencyCountryRepository agencyCountryRepository;

    public AgencyCountyServiceImpl(AgencyCountryRepository agencyCountryRepository) {
        this.agencyCountryRepository = agencyCountryRepository;
    }

    /**
     * save agencyCountry.
     *
     * @param agencyCountry the agencyCountry.
     */
    @Override
    public AgencyCountry save(AgencyCountryDTO agencyCountry) {
        logger.debug("REQ - save agencyCountry");
        return agencyCountryRepository.save(AgencyCountryMapper.INSTANCE.toAgencyCountry(agencyCountry));
    }
    /**
     * update agencyCountry.
     *
     * @param agencyCountry the agencyCountry.
     */
    @Override
    public void update(AgencyCountryDTO agencyCountry) {
        logger.debug("REQ - update agencyCountry");
            if(agencyCountry!=null){
            if(getAgencyCountryById(agencyCountry.getId()).isPresent()) {
                agencyCountryRepository.save(AgencyCountryMapper.INSTANCE.toAgencyCountry(agencyCountry));
            }
        }
    }

    /**
     * get agencyCountry by id.
     *
     * @param id the id.
     * @return agencyCountry found.
     */
    @Override
    public Optional<AgencyCountryDTO> getAgencyCountryById(Long id) {
        logger.debug("REQ - get agencyCountry by id");
        return agencyCountryRepository.findById(id).map(AgencyCountryMapper.INSTANCE::toAgencyCountryDto);
    }

    /**
     * get agencyCountry by idAgencyCountry.
     *
     * @param idAgencyCountry the idAgencyCountry.
     * @return agencyCountry found.
     */
    @Override
    public AgencyCountry getAgencyCountryByIdAgencyCountry(Long idAgencyCountry) {
        return agencyCountryRepository.findByIdAgencyCountry(idAgencyCountry);
    }


    /**
     * get all agencyCountry.
     *
     * @param pageable the pageable.
     * @return list of agencyCountry found.
     */
    @Override
    public Page<AgencyCountryDTO> getAllAgenciesCountry(Pageable pageable) {
        logger.debug("REQ - get all visit page");
        Page<AgencyCountryDTO> agencyCountryDTOs = agencyCountryRepository.findAll(pageable)
                .map(AgencyCountryMapper.INSTANCE::toAgencyCountryDto);
        if(!agencyCountryDTOs.isEmpty()){
            return agencyCountryDTOs;
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    /**
     * get all agencyCountry.
     *
     * @return list of agencyCountry found.
     */
    @Override
    public List<AgencyCountryDTO> getAllAgenciesCountry() {
        logger.debug("REQ - get all visit");
        List<AgencyCountryDTO> agencyCountryList = agencyCountryRepository.findAll()
                .stream()
                .map(AgencyCountryMapper.INSTANCE::toAgencyCountryDto)
                .collect(Collectors.toList());
        if (!agencyCountryList.isEmpty()) {
            return agencyCountryList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * delete agencyCountry.
     *
     * @param agencyCountry the agencyCountry
     */
    @Override
    public void delete(AgencyCountryDTO agencyCountry) {
        logger.debug("REQ - delete visit");
        agencyCountryRepository.delete(AgencyCountryMapper.INSTANCE.toAgencyCountry(agencyCountry));
    }

    /**
     * delete agencyCountry by Id.
     *
     * @param id the id.
     */
    @Override
    public void deleteById(Long id) {
        logger.debug("REQ - delete visit by id");
        agencyCountryRepository.deleteById(id);
    }
}
