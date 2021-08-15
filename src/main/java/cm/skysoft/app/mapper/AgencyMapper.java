package cm.skysoft.app.mapper;

import cm.skysoft.app.domain.Agency;
import cm.skysoft.app.dto.AgencyDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by francis on 2/12/21.
 */
@Mapper(componentModel = "spring", uses = {AgencyRegionMapper.class, AgencyCityMapper.class, AgencyCountryMapper.class, AgencyCountryMapper.class,  NameMapper.class  })
public interface AgencyMapper {

    AgencyMapper INSTANCE = Mappers.getMapper(AgencyMapper.class);

    @Mappings({
            @Mapping(source = "agency.idAgency", target = "id"),
            @Mapping(source = "agency.name", target = "agencyName.fr"),
    })
    AgencyDTO toAgencyDTO(Agency agency);

    List<AgencyDTO> toAgencyDTOs(List<Agency> agencies);

    @Mappings({
            @Mapping(source = "agencyDTO.id", target = "idAgency"),
            @Mapping(source = "agencyDTO.agencyName.fr", target = "name"),
            @Mapping(source = "agencyDTO.agencyName", target = "agencyName")
    })
    Agency toAgency(AgencyDTO agencyDTO);

    List<Agency> toAgencies(List<AgencyDTO> agencyDTOs);
}
