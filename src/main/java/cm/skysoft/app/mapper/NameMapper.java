package cm.skysoft.app.mapper;

import cm.skysoft.app.domain.Name;
import cm.skysoft.app.dto.NameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by francis on 2/18/21.
 */
@Mapper
public interface NameMapper {

    NameMapper INSTANCE = Mappers.getMapper(NameMapper.class);

    NameDTO toNameDTO(Name name);

    List<NameDTO> toNameDTOs(List<Name> names);

    Name toName(NameDTO nameDTO);
}
