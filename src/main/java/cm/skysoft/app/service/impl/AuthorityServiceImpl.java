package cm.skysoft.app.service.impl;

import cm.skysoft.app.mapper.AuthorityMapper;
import cm.skysoft.app.repository.AuthorityRepository;
import cm.skysoft.app.security.AuthoritiesConstants;
import cm.skysoft.app.service.AuthorityService;
import cm.skysoft.app.service.dto.AuthorityDTO;
import cm.skysoft.app.utils.SecurityUtils;
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

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    /**
     * save authority.
     *
     * @param authority the authority
     */
    @Transactional
    @Override
    public void save(AuthorityDTO authority) {
        LOGGER.debug("REQ - save authority");
        authorityRepository.save(AuthorityMapper.INSTANCE.toAuthority(authority));
    }

    /**
     * update authority.
     *
     * @param authority the authority
     */
    @Transactional
    @Override
    public void update(AuthorityDTO authority) {
        LOGGER.debug("REQ - update authority");
        if(getAuthorityById(authority.getName()).isPresent()){
            authorityRepository.save(AuthorityMapper.INSTANCE.toAuthority(authority));
        }
    }

    /**
     * get authority by id.
     *
     * @param id the id
     * @return authority found.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<AuthorityDTO> getAuthorityById(String id) {
        LOGGER.debug("REQ - get authority by name");
        return authorityRepository.findByName(id).map(AuthorityMapper.INSTANCE::toAuthorityDto);
    }

    /**
     * get all authorities.
     *
     * @param pageable the pageable
     * @return list of authorities found.
     */
    @Transactional(readOnly = true)
    @Override
    public Page<AuthorityDTO> getAllAuthorities(Pageable pageable) {
        LOGGER.debug("REQ - get all authorities");
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            return authorityRepository.findAll(pageable).map(AuthorityMapper.INSTANCE::toAuthorityDto);
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    /**
     * get all authorities.
     *
     * @return list of authorities found.
     */
    @Transactional(readOnly = true)
    @Override
    public List<AuthorityDTO> getAllAuthorities() {
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            return authorityRepository.findAll()
                    .stream()
                    .filter(authority -> !AuthoritiesConstants.ADMIN.equals(authority.getName()))
                    .map(AuthorityMapper.INSTANCE::toAuthorityDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }


    /**
     * delete authority.
     *
     * @param authority the authority
     */
    @Transactional
    @Override
    public void delete(AuthorityDTO authority) {
        LOGGER.debug("REQ - delete authority");
        authorityRepository.delete(AuthorityMapper.INSTANCE.toAuthority(authority));
    }

    /**
     * delete authority by Id.
     *
     * @param id the id
     */
    @Transactional
    @Override
    public void deleteById(String id) {
        LOGGER.debug("REQ - delete authority by id");
        authorityRepository.deleteById(id);
    }
}
