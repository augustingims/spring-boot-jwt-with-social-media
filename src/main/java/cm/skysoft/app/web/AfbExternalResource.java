package cm.skysoft.app.web;

/**
 * Created by francis on 2/10/21.
 */

import cm.skysoft.app.domain.User;
import cm.skysoft.app.dto.*;
import cm.skysoft.app.service.AfbExternalService;
import cm.skysoft.app.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing visit type.
 */
@RestController
@RequestMapping("/api/afb")
public class AfbExternalResource {

    private final AfbExternalService afbExternalService;
    private final UserService userService;

    public AfbExternalResource(AfbExternalService afbExternalService, UserService userService) {
        this.afbExternalService = afbExternalService;
        this.userService = userService;
    }

    /**
     * @code Rest to return list of visit type.
     * @return list of VisitTypeDTO.
     */
    @GetMapping("/visits-type")
    public List<VisitTypeDTO> getAllVisitType(){

        return afbExternalService.getListVisitTypeDtos();
    }

    /**
     * @code Rest to return list of user.
     * @return list of UserDTO.
     */
    @GetMapping("/users")
    public List<UserAfbDTO> getAllUserDTO(){

        return afbExternalService.getListUsersDtos();
    }

    /**
     * @code Rest to return list of client by code user.
     * @return list of UserDTO.
     */
    @GetMapping("/clients-by-user-code")
    public List<ClientDTO> getClientByUserCode(){

        return afbExternalService.getListClientByUserCode();
    }

    /**
     * @code Rest to return list of client by code user.
     * @return list of UserDTO.
     */
    @GetMapping("/list-clients-by-user-code")
    public List<ClientDTO> getClientByUserCode(@RequestParam(name = "userCode", required = true) String userCode){

        return afbExternalService.getListClientByUserCode(userCode);
    }

    /**
     * @code Rest to return list of client by code user.
     * @return list of UserDTO.
     */
    @PostMapping("/client-transfert")
    public ClientTransfertResponseDTO getClientByUserCode(@RequestBody ClientTransfertDTO clientTransfertDTO){

        return afbExternalService.clientTransfert(clientTransfertDTO);
    }

    /**
     * @code Rest to return list of client by code user.
     * @return list of UserDTO.
     */
    @GetMapping("/list-user-supervisor-by-user-code")
    public List<UserAfbDTO> getAllUserSupervisorByUserCode(@RequestParam(name = "userCode", required = true) String userCode){

        return afbExternalService.getAllUserSupervisorsByUSerCode(userCode);
    }

    /**
     * @code Rest to return list of client by code user.
     * @return list of UserDTO.
     */
    @GetMapping("/list-user-supervised-by-user-code")
    public List<UserAfbDTO> getAllUserSupervisedByUserCode(){

        User user = userService.getCurrentUser();

        return afbExternalService.getAllUserSupervisedByUSerCode(user.getUserCode());
    }

    /**
     * @code Rest to return list of client information category.
     * @return list of ClientInformationCategoryDTO.
     */
    @GetMapping("/clients-information-categories")
    public List<ClientInformationCategoryDTO> getAllClientInformationCategoryDTO(){

        return afbExternalService.getListClientInformationCategory();
    }

    /**
     * @code Rest to return list info client.
     * @return list of UserDTO.
     */
    @GetMapping("/list-info-client-prepa-visit")
    public ClientPrepaInfosDTO getClientPrePlanInfos(@RequestParam(name = "clientCode", required = true) String clientCode){

        return afbExternalService.getInfoPrepaVisit(clientCode);
    }

    /**
     * @code Rest to return list of client by code client.
     * @return list client by CodeClient
     */
    @GetMapping("/getClientByCodeClient")
    public ClientDTO getClientByCodeClient(@RequestParam(name = "clientCode", required = true) String clientCode){

        return afbExternalService.getClientByClientCode(clientCode);
    }

    /**
     * @code Rest to return list info client.
     * @return list of UserDTO.
     */
    @GetMapping("/list-info-client-by-clientCode")
    public ClientPrepaInfosDTO getInfoClient(@RequestParam(name = "clientCode", required = true) String clientCode,
                                             @RequestParam(name = "categoryId", required = true) String categoryId){

        return afbExternalService.getClientByInfoCategory(categoryId, clientCode);
    }

    /**
     * @code Rest to return list type of commitment
     * @return list type of commitment.
     */
    @GetMapping("/engagement-type")
    public List<EngagementTypeDTO> getEngagementType(){
        return afbExternalService.getEngagementType();
    }

}
