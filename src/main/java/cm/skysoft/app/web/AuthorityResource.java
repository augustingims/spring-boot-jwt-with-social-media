package cm.skysoft.app.web;


import cm.skysoft.app.security.AuthoritiesConstants;
import cm.skysoft.app.service.dto.AuthorityDTO;
import cm.skysoft.app.service.impl.AuthorityServiceImpl;
import cm.skysoft.app.utils.HeaderUtils;
import cm.skysoft.app.utils.PaginationUtils;
import cm.skysoft.app.web.exception.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing the authorities.
 */
@RestController
@RequestMapping("/api")
public class AuthorityResource {

    private final AuthorityServiceImpl authorityService;

    @Value("${application.clientApp.name}")
    private String applicationName;

    public AuthorityResource(AuthorityServiceImpl authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * POST  /authorities : save the authority.
     *
     * @param authority the authority View Model
     *
     */
    @PostMapping("/authorities")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> saveAuthority(@Valid @RequestBody AuthorityDTO authority) {
        if (authority.getName()!=null)  {
            throw new BadRequestAlertException("The authority is already exists", "authority", "idexists");
        }else {
            authorityService.save(authority);
            return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName,"authority.created", authority.getName())).build();
        }
    }

    /**
     * PUT  /authorities : update the authority.
     *
     * @param authority the authority View Model
     *
     */
    @PutMapping("/authorities")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> updateAuthority(@Valid @RequestBody AuthorityDTO authority) {
        if (authority!= null && authority.getName()!=null)  {
            authorityService.save(authority);
            return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName,"authority.updated", authority.getName())).build();
        }else {
            return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName,"authority.notupdated", null)).build();
        }
    }

    /**
     * GET  /authorities : get all the authorities.
     * @param pageable : the pageable
     * @return list of authorities
     */
    @GetMapping("/authorities")
    @ResponseStatus(HttpStatus.OK)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<AuthorityDTO>> getAllAuthoritiesPage(Pageable pageable) {
        final Page<AuthorityDTO> page = authorityService.getAllAuthorities(pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /authoritiesAll : get all the authorities.
     * @return list of authorities
     */
    @GetMapping("/authoritiesAll")
    public ResponseEntity<List<AuthorityDTO>> getAllAuthorities() {
        final List<AuthorityDTO> page = authorityService.getAllAuthorities();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }


    @GetMapping("/authorities/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    public AuthorityDTO getAuthorityById(@PathVariable String id) {
            return authorityService.getAuthorityById(id).orElse(null);
    }

    @DeleteMapping("/authorities/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAuthorityById(@PathVariable String id) {
        authorityService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName,"authority.deleted", id)).build();
    }

    @DeleteMapping("/authorities")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAuthority(@RequestBody AuthorityDTO authority) {
        authorityService.delete(authority);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName,"authority.deleted", authority.getName())).build();
    }
}
