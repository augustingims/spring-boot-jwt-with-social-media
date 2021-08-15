package cm.skysoft.app.mapper;

import cm.skysoft.app.domain.AgencyCity;
import cm.skysoft.app.dto.AgencyCityDTO;
import cm.skysoft.app.dto.NameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by daniel on 2/17/21.
 */

@Mapper(uses = {NameDTO.class})
public interface AgencyCityMapper {

    AgencyCityMapper INSTANCE = Mappers.getMapper(AgencyCityMapper.class);

    @Mappings({
            @Mapping(source = "agencyCity.idAgencyCity", target = "id"),
            @Mapping(source = "agencyCity.agencyCityNameFr", target = "name.fr"),
            @Mapping(source = "agencyCity.agencyCityNameEn", target = "name.en")
    })
    AgencyCityDTO toAgencyCityDto(AgencyCity agencyCity);

    List<AgencyCityDTO>  toAgencyCityDtos(List<AgencyCity> agencyCities);

    @Mappings({
            @Mapping(source = "agencyCityDTO.id", target = "idAgencyCity"),
            @Mapping(source = "agencyCityDTO.name.fr", target = "agencyCityNameFr"),
            @Mapping(source = "agencyCityDTO.name.en", target = "agencyCityNameEn")
    })
    AgencyCity toAgencyCity(AgencyCityDTO agencyCityDTO);

    List<AgencyCity> toAgencyCitys(List<AgencyCityDTO> agencyCityDTOS);


}
