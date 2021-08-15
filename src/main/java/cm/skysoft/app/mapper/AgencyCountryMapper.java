package cm.skysoft.app.mapper;

import cm.skysoft.app.domain.AgencyCountry;
import cm.skysoft.app.dto.AgencyCountryDTO;
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
public interface AgencyCountryMapper {

  AgencyCountryMapper INSTANCE = Mappers.getMapper(AgencyCountryMapper.class);

  @Mappings({
          @Mapping(source = "agencyCountry.idAgencyCountry", target = "id"),
          @Mapping(source = "agencyCountry.agencyCountryName", target = "name.fr"),
  })
  AgencyCountryDTO toAgencyCountryDto(AgencyCountry agencyCountry);

  List<AgencyCountryDTO> toAgencyCountryDTOs(List<AgencyCountry> agencyCountries);

    @Mappings({
            @Mapping(source = "agencyCountryDTO.id", target = "idAgencyCountry"),
            @Mapping(source = "agencyCountryDTO.name.fr", target = "agencyCountryName"),
    })
  AgencyCountry toAgencyCountry(AgencyCountryDTO agencyCountryDTO);

  List<AgencyCountry> toAgencyCountrys(List<AgencyCountryDTO> agencyCountryDTOS);

}
