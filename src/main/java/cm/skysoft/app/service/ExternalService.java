package cm.skysoft.app.service;

import cm.skysoft.app.domain.*;
import cm.skysoft.app.dto.AgencyCityDTO;
import cm.skysoft.app.dto.AgencyCountryDTO;
import cm.skysoft.app.dto.AgencyDTO;
import cm.skysoft.app.dto.AgencyRegionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by francis on 2/23/21.
 */
@Service
public class ExternalService {


    private final AgencyCountryService agencyCountryService;
    private final AgencyRegionService agencyRegionService;
    private final AgencyCityService agencyCityService;
    private final AgencyService agencyService;
    private final AgencyUserService agencyUserService;


    public ExternalService(AgencyCountryService agencyCountryService, AgencyRegionService agencyRegionService, AgencyCityService agencyCityService, AgencyService agencyService, AgencyUserService agencyUserService) {
        this.agencyCountryService = agencyCountryService;
        this.agencyRegionService = agencyRegionService;
        this.agencyCityService = agencyCityService;
        this.agencyService = agencyService;
        this.agencyUserService = agencyUserService;
    }

    @Transactional
    public AgencyCountry saveAgencyCountry(AgencyCountryDTO agencyCountryDTO) {

        AgencyCountry agencyCountry = getAgencyCountryByIdOrIdAgencyCountry(agencyCountryDTO);

        if (agencyCountry != null) {
            return agencyCountry;
        } else {
            return agencyCountryService.save(agencyCountryDTO);
        }
    }

    @Transactional
    public AgencyRegion saveAgencyRegion(AgencyRegionDTO agencyRegionDTO) {

        AgencyRegion agencyRegion = getAgencyRegionByIdOrIdAgencyRegion(agencyRegionDTO);

        if (agencyRegion != null) {
            return agencyRegion;
        } else {
            return agencyRegionService.save(agencyRegionDTO);
        }
    }

    @Transactional
    public AgencyCity saveAgencyCity(AgencyCityDTO agencyCityDTO) {

        AgencyCity agencyCity = getAgencyCityByIdOrIdAgencyRegion(agencyCityDTO);

        if (agencyCity != null) {
            return agencyCity;
        } else {
            return agencyCityService.save(agencyCityDTO);
        }
    }

    @Transactional
    public Agency saveAgency(AgencyDTO agencyDTO) {

        Agency agency = agencyService.getAgencyByIdAgency(agencyDTO.getId());

        if (agency != null) {
            return agency;
        } else {
            Agency a =  new Agency();

            a.setName(agencyDTO.getAgencyName().getFr());
            a.setAgencyCode(agencyDTO.getAgencyCode());
            a.setIdAgency(agencyDTO.getId());
            a.setAgencyCountry(saveAgencyCountry(agencyDTO.getAgencyCountry()));
            a.setAgencyRegion(saveAgencyRegion(agencyDTO.getAgencyRegion()));
            a.setAgencyCity(saveAgencyCity(agencyDTO.getAgencyCity()));

            agency = agencyService.save(a);
        }
        return  agency;
    }

    @Transactional
    public AgencyCountry getAgencyCountryByIdOrIdAgencyCountry(AgencyCountryDTO agencyCountryDTO) {

        AgencyCountry agencyCountry = null;

        if (agencyCountryDTO.getIdAgencyCountry() != null) {
            agencyCountry = agencyCountryService.getAgencyCountryByIdAgencyCountry(agencyCountryDTO.getIdAgencyCountry());
        } else if (agencyCountryDTO.getId() != null) {
            agencyCountry = agencyCountryService.getAgencyCountryByIdAgencyCountry(agencyCountryDTO.getId());
        }
        return agencyCountry;
    }

    @Transactional
    public AgencyRegion getAgencyRegionByIdOrIdAgencyRegion(AgencyRegionDTO agencyRegionDTO) {

        AgencyRegion agencyRegion = null;

        if (agencyRegionDTO.getIdAgencyRegion() != null) {
            agencyRegion = agencyRegionService.getAgencyRegionByIdAgencyRegion(agencyRegionDTO.getIdAgencyRegion());
        } else if (agencyRegionDTO.getId() != null) {
            agencyRegion = agencyRegionService.getAgencyRegionByIdAgencyRegion(agencyRegionDTO.getId());
        }
        return agencyRegion;
    }

    @Transactional
    public AgencyCity getAgencyCityByIdOrIdAgencyRegion(AgencyCityDTO agencyCityDTO) {

        AgencyCity agencyCity = null;

        if (agencyCityDTO.getIdAgencyCity() != null) {
            agencyCity = agencyCityService.getAgencyCityByIdAgencyCity(agencyCityDTO.getIdAgencyCity());
        } else if (agencyCityDTO.getId() != null) {
            agencyCity = agencyCityService.getAgencyCityByIdAgencyCity(agencyCityDTO.getId());
        }
        return agencyCity;
    }

    @Transactional
    public AgencyUser getAgencyUserByIdUserAndIdAgency(User user, Agency agency){

        Optional<AgencyUser> agencyUser = agencyUserService.getAgencyUserByIdUserAndIdAgency(user.getId(), agency.getId());

        if (!agencyUser.isPresent()) {
            AgencyUser au = new AgencyUser();
            au.setAgency(agency);
            au.setUser(user);
            return agencyUserService.save(au);
        }
        return agencyUser.get();
    }

    @Transactional
    public Optional<AgencyUser> getAgencyUserByUserYd(User user, Agency agency){
        Optional<AgencyUser> agencyUser = agencyUserService.getAgencyUserByIdUser(user.getId());

        agencyUser.ifPresent(value -> value.setAgency(agency));

        return agencyUser;
    }
}
