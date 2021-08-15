package cm.skysoft.app.mapper;

import cm.skysoft.app.domain.UserAfbs;
import cm.skysoft.app.dto.UserAfbDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by francis on 2/13/21.
 */
@Mapper(componentModel = "spring", uses = {AgencyMapper.class} ,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface UserAfbMapper {

    UserAfbMapper INSTANCE = Mappers.getMapper(UserAfbMapper.class);

    @Mappings({
            @Mapping(source = "userAfb.idUserAfb", target = "id"),
            @Mapping(source = "userAfb.agency", target = "agency")
    })
    UserAfbDTO toUserAfbDTO(UserAfbs userAfb);

    List<UserAfbDTO> toUserAfbDTOs(List<UserAfbs> userAfbs);

    @Mappings({
            @Mapping(source = "userAfbDTO.id", target = "idUserAfb"),
            @Mapping(source = "userAfbDTO.agency", target = "agency"),
    })
    UserAfbs toUserAfb(UserAfbDTO userAfbDTO);

    List<UserAfbs> toUserAfbs(List<UserAfbDTO> userAfbDTOs);
}
