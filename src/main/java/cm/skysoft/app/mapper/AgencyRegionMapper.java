package cm.skysoft.app.mapper;

import cm.skysoft.app.domain.AgencyRegion;
import cm.skysoft.app.dto.AgencyRegionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by daniel on 2/17/21.
 */
/*@Mapper( componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)*/
@Mapper
public interface AgencyRegionMapper {

    AgencyRegionMapper INSTANCE = Mappers.getMapper(AgencyRegionMapper.class);

    @Mappings({
            @Mapping(source = "agencyRegion.idAgencyRegion", target = "id"),
            @Mapping(source = "agencyRegion.agencyRegionName", target = "name.fr")
    })

    AgencyRegionDTO toAgencyRegionDTO(AgencyRegion agencyRegion);

    List<AgencyRegionDTO> toAgencyRegionDTOs(List<AgencyRegion> agencyRegions);

    @Mappings({
            @Mapping(source = "id", target = "idAgencyRegion"),
            @Mapping(source = "agencyRegionDTO.name.fr", target = "agencyRegionName")
    })
    AgencyRegion toAgencyRegion(AgencyRegionDTO agencyRegionDTO);

    List<AgencyRegion> toAgencyRegions(List<AgencyRegionDTO> agencyRegionDTOS);


}
