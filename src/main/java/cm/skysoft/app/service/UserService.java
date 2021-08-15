package cm.skysoft.app.service;

import cm.skysoft.app.criteria.AgencyUserCriteria;
import cm.skysoft.app.criteria.UserCriteriaDTO;
import cm.skysoft.app.domain.*;
import cm.skysoft.app.dto.UserAfbDTO;
import cm.skysoft.app.exception.EmailAlreadyUsedException;
import cm.skysoft.app.exception.InvalidPasswordException;
import cm.skysoft.app.exception.UsernameAlreadyUsedException;
import cm.skysoft.app.mapper.UserMapper;
import cm.skysoft.app.repository.AgencyUserRepository;
import cm.skysoft.app.repository.AuthorityRepository;
import cm.skysoft.app.repository.UserRepository;
import cm.skysoft.app.repository.specification.AgencyUserSpecification;
import cm.skysoft.app.repository.specification.UserSpecification;
import cm.skysoft.app.security.AuthoritiesConstants;
import cm.skysoft.app.service.dto.RequestUserDTO;
import cm.skysoft.app.service.dto.UserDTO;
import cm.skysoft.app.utils.ConstantsUtils;
import cm.skysoft.app.utils.RandomUtils;
import cm.skysoft.app.utils.SecurityUtils;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AgencyUserRepository agencyUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final CacheManager cacheManager;
    private final ExternalService externalService;
    private final UserMapper userMapper;
    private final LogsService logsService;

    public UserService(UserRepository userRepository, AgencyUserRepository agencyUserRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager, ExternalService externalService, UserMapper userMapper, LogsService logsService) {
        this.userRepository = userRepository;
        this.agencyUserRepository = agencyUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.externalService = externalService;
        this.userMapper = userMapper;
        this.logsService = logsService;
    }

    public Optional<User> activateRegistration(String key) {
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtils.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtils.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        return newUser;
    }

    public User registerUserAfb(UserAfbDTO userAfbDTO, String password, String login, String token) {

        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login.toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userAfbDTO.getFirstName());
        newUser.setLastName(userAfbDTO.getLastName());
        newUser.setToken(token);
        newUser.setPositionName(userAfbDTO.getPositionName());
        newUser.setIcn(userAfbDTO.getIcn());
        newUser.setPhoneNumber(userAfbDTO.getPhoneNumber());
        newUser.setUserCode(userAfbDTO.getUserCode());
        newUser.setIdUserAfb(userAfbDTO.getId());

        // new user is active
        newUser.setActivated(true);
        newUser.setLangKey(ConstantsUtils.DEFAULT_LANGUAGE);
        Set<Authority> authorities = new HashSet<>();
        Optional<Authority> authority = authorityRepository.findById(String.format(ConstantsUtils.FORMAT_TWO_STRING, ConstantsUtils.PREFIX_ROLE,userAfbDTO.getPositionName()));

        if (authority.isPresent()) {
            authorities.add(authority.get());
        } else {
            Authority auth = new Authority();
            auth.setName(String.format(ConstantsUtils.FORMAT_TWO_STRING, ConstantsUtils.PREFIX_ROLE,userAfbDTO.getPositionName()));
            auth = authorityRepository.save(auth);
            authorities.add(auth);
        }

        Agency agency = externalService.saveAgency(userAfbDTO.getAgency());


        newUser.setAuthorities(authorities);
        newUser = userRepository.save(newUser);

        externalService.getAgencyUserByIdUserAndIdAgency(newUser, agency);

        if (newUser.getId() != null) {
            logsService.save(new Logs(newUser, "Enregistrement de l'utilisateur " + newUser.getFirstName().toUpperCase() + " " + newUser.getLastName().toUpperCase() + " dans le système", Logs.ENREGISTREMENT_USER));
        }

        this.clearUserCaches(newUser);
        return newUser;
    }


    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
             return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(RequestUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setIcn(userDTO.getIcn());
        user.setPositionName(userDTO.getPositionName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(ConstantsUtils.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtils.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        return user;
    }

    public User createUserSocial(UserDTO userDTO, String password) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(ConstantsUtils.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtils.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if(userDTO.getPhoneNumber() != null) {
                    user.setPhoneNumber(userDTO.getPhoneNumber());
                }
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                this.clearUserCaches(user);



                return user;
            })
            .map(UserDTO::new);
    }

    public User changeInfoUser(UserDTO userDTO) {
        Optional<User> user = userRepository.findOneByLogin(userDTO.getLogin());

        if(user.isPresent()) {
            user.get().setPhoneNumber(userDTO.getPhoneNumber());
            user.get().setEmail(userDTO.getEmail());
        }
        return user.map(userRepository::save).orElse(null);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
        });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
            });
    }


    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(UserCriteriaDTO userCriteriaDTO, Pageable pageable) {
        return userRepository.findAll(UserSpecification.getSpecification(userCriteriaDTO), pageable).map(UserDTO::new);
    }

    public List<UserDTO> getAllUsers() {

        List<User> users = new ArrayList<>();

        User currentUser =  getCurrentUser();
        Optional<User> adminUser = getUserWithAuthoritiesByLogin(ConstantsUtils.ADMIN_USER_LOGIN);

        adminUser.ifPresent(users::add);

        users.add(currentUser);

        List <User> userList = userRepository.findAll();

        userList.removeAll(users);

        return userMapper.usersToUserDTOs(userList);
    }

    public List<User> findAllUser() {

        List<User> users = new ArrayList<>();

        User currentUser =  getCurrentUser();
        Optional<User> adminUser = getUserWithAuthoritiesByLogin(ConstantsUtils.ADMIN_USER_LOGIN);

        adminUser.ifPresent(users::add);

        users.add(currentUser);

        List <User> userList = userRepository.findAll();

        userList.removeAll(users);

        return userList;
    }

    public List<User> getAllUsersListForAdmin(AgencyUserCriteria agencyUserCriteria) {

        List<User> users = new ArrayList<>();
        List<User> userList = new ArrayList<>();

        User currentUser =  getCurrentUser();
        Optional<User> adminUser = getUserWithAuthoritiesByLogin(ConstantsUtils.ADMIN_USER_LOGIN);

        adminUser.ifPresent(users::add);

        users.add(currentUser);

        List<AgencyUser> userPage = agencyUserRepository.findAll(AgencyUserSpecification.getSpecification(agencyUserCriteria));
        if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) {
            /*for (AgencyUser u : userPage) {
                userList.add(u.getUser());
            }
            userList.removeAll(users);
            return userList;*/
            if (agencyUserCriteria.getLogin() != null || agencyUserCriteria.getUserCode() != null || agencyUserCriteria.getIdAgency() != null || agencyUserCriteria.getIdAgencyRegion() != null || agencyUserCriteria.getProfil() != null) {

                for (AgencyUser u : userPage) {
                    userList.add(u.getUser());
                }
                userList.removeAll(users);
                return userList;
            } else {
                return new ArrayList<>();
            }
        } else {
            if (userPage.isEmpty()) {
                userList = userRepository.findAll();
                userList.removeAll(users);
                return userList;
            } else {
                for (AgencyUser u : userPage) {
                    userList.add(u.getUser());
                }
                userList.removeAll(users);
                return userList;
            }
        }
    }

    public List<User> getAllUsersList(UserCriteriaDTO userCriteriaDTO) {

        List<User> users = new ArrayList<>();

        User currentUser =  getCurrentUser();
        Optional<User> adminUser = getUserWithAuthoritiesByLogin(ConstantsUtils.ADMIN_USER_LOGIN);

        adminUser.ifPresent(users::add);

        users.add(currentUser);

        if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
            List<User> userPage = userRepository.findAll(UserSpecification.getSpecification(userCriteriaDTO));
            if(userCriteriaDTO.getLogin() != null || userCriteriaDTO.getUserCode() != null || userCriteriaDTO.getIdAgency() != null){
                userPage.removeAll(users);
                return userPage;
            } else {
                return new ArrayList<>();
            }
        } else {
            List<User> userPage = userRepository.findAll(UserSpecification.getSpecification(userCriteriaDTO));
            if (userPage.isEmpty()) {
                List<User> userList = userRepository.findAll();
                userList.removeAll(users);
                return userList;
            } else {
                userPage.removeAll(users);
                return userPage;
            }
        }
    }

    public List<UserDTO> getAllUsersSuggestion() {

        List<User> users = new ArrayList<>();

        User currentUser =  getCurrentUser();
        Optional<User> adminUser = getUserWithAuthoritiesByLogin(ConstantsUtils.ADMIN_USER_LOGIN);

        adminUser.ifPresent(users::add);

        users.add(currentUser);

        List <User> userList = userRepository.findAll();

        return userMapper.usersToUserDTOs(userList);
    }

    public Optional<User> getUserAuthenticate() {
        User user = getCurrentUser();
        return userRepository.findOneByLogin(user.getLogin());
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByUserCode(String userCode) {
        return userRepository.findAllByUserCode(userCode);
    }

    @Transactional
    public Optional<User> getUserByIdUserAfb(Long idUserAfb) {
        return userRepository.findOneByIdUserAfb(idUserAfb);
    }

    @Transactional
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public User getCurrentUser() {
        String login = SecurityUtils.getCurrentUserLogin().orElse(null);
        if(login != null) {
            return getUserWithAuthoritiesByLogin(login).orElse(null);
        } else {
            return null;
        }
    }
}
