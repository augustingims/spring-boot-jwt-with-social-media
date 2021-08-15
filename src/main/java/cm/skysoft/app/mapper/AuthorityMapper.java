package cm.skysoft.app.mapper;

import cm.skysoft.app.domain.Authority;
import cm.skysoft.app.service.dto.AuthorityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthorityMapper {

    AuthorityMapper INSTANCE = Mappers.getMapper( AuthorityMapper.class );

    AuthorityDTO toAuthorityDto(Authority authority);

    List<AuthorityDTO> toAuthorityDTOs(List<Authority> authorities);

    Set<AuthorityDTO> toAuthorityDTOs(Set<Authority> authorities);

    Authority toAuthority(AuthorityDTO authorityDTO);

    Set<Authority> toSetAutorities(List<AuthorityDTO> authorityDTOS);

}
