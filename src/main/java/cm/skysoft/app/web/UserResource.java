package cm.skysoft.app.web;

import cm.skysoft.app.criteria.UserCriteriaDTO;
import cm.skysoft.app.domain.Agency;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.repository.UserRepository;
import cm.skysoft.app.security.AuthoritiesConstants;
import cm.skysoft.app.service.*;
import cm.skysoft.app.service.dto.RequestUserDTO;
import cm.skysoft.app.service.dto.UserDTO;
import cm.skysoft.app.utils.HeaderUtils;
import cm.skysoft.app.utils.PaginationUtils;
import cm.skysoft.app.utils.ResponseUtils;
import cm.skysoft.app.web.exception.BadRequestAlertException;
import cm.skysoft.app.web.exception.EmailAlreadyUsedException;
import cm.skysoft.app.web.exception.LoginAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {
    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey"));

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${application.clientApp.name}")
    private String applicationName;

    private final UserService userService;
    private final ExternalService externalService;
    private final AgencyService agencyService;

    private final UserRepository userRepository;
    private final AfbExternalService afbExternalService;

    private final MailService mailService;

    public UserResource(UserService userService, ExternalService externalService, AgencyService agencyService, UserRepository userRepository, AfbExternalService afbExternalService, MailService mailService) {
        this.userService = userService;
        this.externalService = externalService;
        this.agencyService = agencyService;
        this.userRepository = userRepository;
        this.afbExternalService = afbExternalService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody RequestUserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtils.createAlert(applicationName,  "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);

        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        if(userDTO.getIdAgency() != null) {
            Optional<Agency> agency = agencyService.getAgencyById(userDTO.getIdAgency());

            if (updatedUser.isPresent()) {
                Optional<User> user = userService.getUserById(updatedUser.get().getId());
                user.flatMap(user1 -> agency).ifPresent(value -> externalService.getAgencyUserByUserYd(user.get(), value));
            }
        }

        return ResponseUtils.wrapOrNotFound(updatedUser,
            HeaderUtils.createAlert(applicationName, "A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()));
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable,
                                                     @RequestParam(value = "login", required = false) String login,
                                                     @RequestParam(value = "userCode", required = false) String userCode,
                                                     @RequestParam(value = "positionName", required = false) String positionName) {

        UserCriteriaDTO userCriteriaDTO = new UserCriteriaDTO();

        userCode = (userCode == null || userCode.equals("") || userCode.equals("null")) ? null : userCode;
        login = (login == null || login.equals("") || login.equals("null")) ? null : login;
        positionName = (positionName == null || positionName.equals("") || positionName.equals("null")) ? null : positionName;

        if(positionName != null) {
            userCriteriaDTO.setPositionName(positionName);
        }
        if(login != null) {
            userCriteriaDTO.setLogin(login);
        }
        if(userCode != null) {
            userCriteriaDTO.setUserCode(userCode);
        }

        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<UserDTO> page = userService.getAllManagedUsers(userCriteriaDTO, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/usersAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> userDTOs = userService.getAllUsers();
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/findUserByLogin")
    public ResponseEntity<Optional<User>> findUserByLogin() {
        return ResponseEntity.ok().body(userService.getUserAuthenticate());
    }

    @PutMapping("/changeInfoUser")
    public ResponseEntity<User> changeInfoUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok().body(userService.changeInfoUser(userDTO));
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/usersAllSuggestion")
    public ResponseEntity<List<UserDTO>> usersAllSuggestion() {

        List<UserDTO> userDTOs = userService.getAllUsersSuggestion();
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtils.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    @GetMapping("/getUserByUserCode/{userCode}")
    public ResponseEntity<UserDTO> getUserByUserCode(@PathVariable String userCode) {
        log.debug("REST request to get User : {}", userCode);
        return ResponseUtils.wrapOrNotFound(
            userService.getUserByUserCode(userCode)
                .map(UserDTO::new));
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtils.createAlert(applicationName,  "A user is deleted with identifier " + login, login)).build();
    }

    @GetMapping("/getAllUserSupervised")
    public ResponseEntity<List<User>> getAllUserSupervised(){
        return ResponseEntity.ok().body(afbExternalService.getAllSupervised());
    }
}
