package cm.skysoft.app.service.impl;

import cm.skysoft.app.config.ApplicationProperties;
import cm.skysoft.app.domain.Agency;
import cm.skysoft.app.domain.Authority;
import cm.skysoft.app.domain.Logs;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.dto.*;
import cm.skysoft.app.repository.AuthorityRepository;
import cm.skysoft.app.repository.UserRepository;
import cm.skysoft.app.service.AfbExternalService;
import cm.skysoft.app.service.ExternalService;
import cm.skysoft.app.service.LogsService;
import cm.skysoft.app.service.UserService;
import cm.skysoft.app.utils.ConstantsUtils;
import cm.skysoft.app.utils.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by francis on 2/10/21.
 */
@Service
@Transactional
public class AfbExternalServiceImpl implements AfbExternalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfbExternalServiceImpl.class);
    private static final String codeUser = "userCode";
    private static final String codeClient = "clientCode";
    private static final String idCategory = "categoryId";

    private final ApplicationProperties applicationProperties;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final LogsService logsService;
    private final ExternalService externalService;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    public AfbExternalServiceImpl(ApplicationProperties applicationProperties, UserService userService, PasswordEncoder passwordEncoder, LogsService logsService, ExternalService externalService, AuthorityRepository authorityRepository, UserRepository userRepository, CacheManager cacheManager) {
        this.applicationProperties = applicationProperties;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.logsService = logsService;
        this.externalService = externalService;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * Get All visit type.
     *
     * @return List of VisitTypeDTO.
     */
    @Override
    public List<VisitTypeDTO> getListVisitTypeDtos() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VisitTypeDTO[]> listVisitTypeDTO = null;

        String visitTypeApi = applicationProperties.getAfbExternalApi().getVisitTypeApi();

        HttpHeaders authenticationHeaders = new HttpHeaders();
        authenticationHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            listVisitTypeDTO  = restTemplate.getForEntity(visitTypeApi, VisitTypeDTO[].class);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return listVisitTypeDTO != null? Arrays.asList(Objects.requireNonNull(listVisitTypeDTO.getBody())): new ArrayList<>();
    }

    /**
     * Get All user.
     *
     * @return List of UserAfbDTO.
     */
    @Override
    public List<UserAfbDTO> getListUsersDtos() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserAfbDTO[]> userAfbDTO = null;

        String usersApi = applicationProperties.getAfbExternalApi().getUsersApi();

        try {
            userAfbDTO  = restTemplate.getForEntity(usersApi, UserAfbDTO[].class);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return userAfbDTO != null? Arrays.asList(Objects.requireNonNull(userAfbDTO.getBody())): new ArrayList<>();
    }

    /**
     * Get clients by userCode.
     *
     * @return List of ClientDTO.
     */
    @Override
    public List<ClientDTO> getListClientByUserCode() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ClientDTO[]> clientDTO = null;

        User user = userService.getCurrentUser();
        Map<String, String> params = new HashMap<>();
        params.put(codeUser, user.getUserCode());

        String clientByUserCodeApi = applicationProperties.getAfbExternalApi().getClientByCodeUserApi();

        try {
            clientDTO  = restTemplate.getForEntity(clientByUserCodeApi, ClientDTO[].class, params);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return clientDTO != null? Arrays.asList(Objects.requireNonNull(clientDTO.getBody())): new ArrayList<>();
    }

    /**
     * Get clients by userCode.
     *
     * @return List of ClientDTO.
     */
    @Override
    public List<ClientDTO> getListClientByUserCode(String userCode) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ClientDTO[]> clientDTO = null;

        Map<String, String> params = new HashMap<>();
        params.put(codeUser, userCode);

        String clientByUserCodeApi = applicationProperties.getAfbExternalApi().getClientByCodeUserApi();

        try {
            clientDTO  = restTemplate.getForEntity(clientByUserCodeApi, ClientDTO[].class, params);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return clientDTO != null? Arrays.asList(Objects.requireNonNull(clientDTO.getBody())): new ArrayList<>();
    }


    /**
     * Get All user supervisor by userCode.
     *
     * @return List of UserAfbDTO.
     */
    @Override
    public List<UserAfbDTO> getListUserSupervisorByUserCode() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserAfbDTO[]> userAfbDTO = null;


        User user = userService.getCurrentUser();
        Map<String, String> params = new HashMap<>();
        params.put(codeUser, user.getUserCode());

        String clientByUserCodeApi = applicationProperties.getAfbExternalApi().getClientByCodeUserApi();

        try {
            userAfbDTO  = restTemplate.getForEntity(clientByUserCodeApi, UserAfbDTO[].class, params);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return userAfbDTO != null? Arrays.asList(Objects.requireNonNull(userAfbDTO.getBody())): new ArrayList<>();
    }

    /**
     * @param loginUserAfbDTO by loginUserAfbDTO
     * @return userAfb connected.
     */
    @Override
    public AuthenticateUserAfbResponseDTO authenticateUserAfb(LoginUserAfbDTO loginUserAfbDTO) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AuthenticateUserAfbResponseDTO> authenticateUserAfbResponseDTO = null;

        String authenticateUserAfbApi = applicationProperties.getAfbExternalApi().getAuthenticateUserAfbApi();

        try {
            authenticateUserAfbResponseDTO  = restTemplate.postForEntity(authenticateUserAfbApi, loginUserAfbDTO, AuthenticateUserAfbResponseDTO.class);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return authenticateUserAfbResponseDTO != null? authenticateUserAfbResponseDTO.getBody(): null;
    }

    /**
     * @param userCode
     * @return userAfb connected.
     */
    @Override
    public List<UserAfbDTO> getAllUserSupervisorsByUSerCode(String userCode) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserAfbDTO[]> userAfbDTO = null;

        Map<String, String> params = new HashMap<>();
        params.put(codeUser, userCode);

        String clientByUserCodeApi = applicationProperties.getAfbExternalApi().getUserSupervisorByUsercodeApi();

        try {
            userAfbDTO  = restTemplate.getForEntity(clientByUserCodeApi, UserAfbDTO[].class, params);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return userAfbDTO != null? Arrays.asList(Objects.requireNonNull(userAfbDTO.getBody())): new ArrayList<>();
    }

    @Override
    public List<ClientInformationCategoryDTO> getListClientInformationCategory() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ClientInformationCategoryDTO[]> listClientInformationCategoryDTO = null;

        String clientInformationCategoryApi = applicationProperties.getAfbExternalApi().getClientInformationCategoryApi();

        HttpHeaders authenticationHeaders = new HttpHeaders();
        authenticationHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            listClientInformationCategoryDTO  = restTemplate.getForEntity(clientInformationCategoryApi, ClientInformationCategoryDTO[].class);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return listClientInformationCategoryDTO != null? Arrays.asList(Objects.requireNonNull(listClientInformationCategoryDTO.getBody())): new ArrayList<>();
    }

    @Override
    public List<ProductDTO> getListProducts() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ProductDTO[]> listproductsDTO = null;

        String productsApi = applicationProperties.getAfbExternalApi().getProductsListAfbApi();

        HttpHeaders authenticationHeaders = new HttpHeaders();
        authenticationHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            listproductsDTO  = restTemplate.getForEntity(productsApi, ProductDTO[].class);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return listproductsDTO != null? Arrays.asList(Objects.requireNonNull(listproductsDTO.getBody())): new ArrayList<>();
    }

    @Override
    public List<MeansUsedForVisitDTO> getListMeansUsedForVisit() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MeansUsedForVisitDTO[]> meansUsedForVisitDTO = null;

        String meansUsedForVisitApi = applicationProperties.getAfbExternalApi().getListMeansUsedForVisitAfbApi();

        HttpHeaders authenticationHeaders = new HttpHeaders();
        authenticationHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            meansUsedForVisitDTO  = restTemplate.getForEntity(meansUsedForVisitApi, MeansUsedForVisitDTO[].class);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return meansUsedForVisitDTO != null? Arrays.asList(Objects.requireNonNull(meansUsedForVisitDTO.getBody())): new ArrayList<>();
    }

    @Override
    public List<UserAfbDTO> getAllUserSupervisedByUSerCode(String userCode) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserAfbDTO[]> userAfbDTO = null;

        Map<String, String> params = new HashMap<>();
        params.put(codeUser, userCode);

        String clientByUserCodeApi = applicationProperties.getAfbExternalApi().getUserSupervisedByUserCodeApi();

        try {
            userAfbDTO  = restTemplate.getForEntity(clientByUserCodeApi, UserAfbDTO[].class, params);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return userAfbDTO != null? Arrays.asList(Objects.requireNonNull(userAfbDTO.getBody())): new ArrayList<>();
    }

    @Override
    public ClientPrepaInfosDTO getInfoPrepaVisit(String clientCode) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ClientPrepaInfosDTO> infoClient = null;

        Map<String, String> params = new HashMap<>();
        params.put(codeClient, clientCode);

        String infoClientApi = applicationProperties.getAfbExternalApi().getPrePaInfosByClientcodeApi();

        try {
            infoClient  = restTemplate.getForEntity(infoClientApi, ClientPrepaInfosDTO.class, params);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return  infoClient != null ? infoClient.getBody(): new ClientPrepaInfosDTO();
    }

    @Override
    public ClientDTO getClientByClientCode(String clientCode) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ClientDTO> client = null;

        Map<String, String> params = new HashMap<>();
        params.put(codeClient, clientCode);

        String clientApi = applicationProperties.getAfbExternalApi().getClientByCodeclientAfbApi();

        try {
            client  = restTemplate.getForEntity(clientApi, ClientDTO.class, params);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return client != null? client.getBody(): new ClientDTO();
    }

    @Override
    public ClientTransfertResponseDTO clientTransfert(ClientTransfertDTO clientTransfertDTO) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ClientTransfertResponseDTO> responseEntity = null;
        ResponseEntity<byte[]> clientTranfertAfbResponseDTO = null;
        ClientTransfertResponseDTO clientTransfertResponseDTO = new ClientTransfertResponseDTO();

        User user = userService.getCurrentUser();

        clientTransfertDTO.setInitialManagerCode(user.getUserCode());

        String clientTransfertAfbApi = applicationProperties.getAfbExternalApi().getClientTransfert();
        String json;

        try {
            clientTranfertAfbResponseDTO  = restTemplate.postForEntity(clientTransfertAfbApi, clientTransfertDTO, byte[].class);
            byte[] bytes = clientTranfertAfbResponseDTO.getBody();
            assert bytes != null;
            json = new String(bytes);
            switch (json) {
                case "OK":
                    clientTransfertResponseDTO.setMessage("Le transfert c'est effectué avec succès");
                    break;
                case "CN":
                    clientTransfertResponseDTO.setMessage("Le code client envoye n'existe pas ou est incorrect");
                    break;
                case "G1":
                    clientTransfertResponseDTO.setMessage("Le code du gestionnaire initial n'existe pas ou est incorrect");
                    break;
                case "G2":
                    clientTransfertResponseDTO.setMessage("Le code du gestionnaire final n'existe pas ou est incorrect");
                    break;
                default:
                    clientTransfertResponseDTO.setMessage("Le gestionnaire initial n'est pas le gestionnaire du client");
                    break;
            }
            responseEntity = ResponseEntity.ok().body(clientTransfertResponseDTO);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return responseEntity != null ? responseEntity.getBody() : null;
    }

    @Override
    public ClientPrepaInfosDTO getClientByInfoCategory(String categoryId, String clientCode) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ClientPrepaInfosDTO> infoClient = null;

        Map<String, String> params = new HashMap<>();
        params.put(codeClient, clientCode);
        params.put(idCategory, categoryId);

        String infoClientApi = applicationProperties.getAfbExternalApi().getClientByInfoCategoryAfbApi();

        try {
            infoClient  = restTemplate.getForEntity(infoClientApi, ClientPrepaInfosDTO.class, params);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return infoClient != null? infoClient.getBody(): null;
    }

    @Override
    public List<EngagementTypeDTO> getEngagementType() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EngagementTypeDTO[]> engagementTypeDTO = null;

        String engagementTypeAfbApi = applicationProperties.getAfbExternalApi().getListEngagementTypeAfbApi();

        HttpHeaders authenticationHeaders = new HttpHeaders();
        authenticationHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            engagementTypeDTO  = restTemplate.getForEntity(engagementTypeAfbApi, EngagementTypeDTO[].class);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }

        return engagementTypeDTO != null? Arrays.asList(Objects.requireNonNull(engagementTypeDTO.getBody())): new ArrayList<>();
    }

    @Override
    public void saveUserAfb(){
        List<UserAfbDTO> userAfbDTOS = getListUsersDtos();
        String codeUser = "";
        String login = "";
        User newUser;

        for(UserAfbDTO userAfbDTO : userAfbDTOS){

            codeUser = userAfbDTO.getUserCode();

            login = userAfbDTO.getFirstName().toLowerCase() + "_" + userAfbDTO.getLastName().toLowerCase();
            Optional<User> user = userRepository.findAllByUserCode(codeUser.toLowerCase());

            if(!user.isPresent()){
                newUser = new User();
                String encryptedPassword = passwordEncoder.encode(userAfbDTO.getFirstName().toLowerCase() + "" + userAfbDTO.getLastName().toLowerCase() + "password");
                newUser.setLogin(login);
                newUser.setPassword(encryptedPassword);
                newUser.setFirstName(userAfbDTO.getFirstName());
                newUser.setLastName(userAfbDTO.getLastName());
                newUser.setToken(null);
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
            }
        }
    }

    @Override
    public List<User> getAllSupervised(){
        User user = userService.getCurrentUser();

        if(!SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")) {
            List<UserAfbDTO> userAfbDTOList = getAllUserSupervisedByUSerCode(user.getUserCode());
            return getUsers(userAfbDTOList);
        } else {
            List<UserAfbDTO> userAfbDTOList = getListUsersDtos();
            return getUsers(userAfbDTOList);
        }
    }

    @NotNull
    private List<User> getUsers(List<UserAfbDTO> userAfbDTOList) {
        List<User> userList = userService.findAllUser();
        List<User> users = new ArrayList<>();

        if (!userAfbDTOList.isEmpty()) {
            if (userList.size() > userAfbDTOList.size()) {
                for (User userDTO : userList) {
                    for (UserAfbDTO userAfbDTO : userAfbDTOList) {
                        if (userDTO.getUserCode().equals(userAfbDTO.getUserCode())) {
                            users.add(userDTO);
                        }
                    }
                }
            } else {
                for (UserAfbDTO userAfbDTO : userAfbDTOList) {
                    for (User userDTO : userList) {
                        if (userDTO.getUserCode().equals(userAfbDTO.getUserCode())) {
                            users.add(userDTO);
                        }
                    }
                }

            }
            return users;
        } else {
            return new ArrayList<>();
        }
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
