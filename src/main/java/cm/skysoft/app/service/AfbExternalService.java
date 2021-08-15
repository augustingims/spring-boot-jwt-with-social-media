package cm.skysoft.app.service;

import cm.skysoft.app.domain.User;
import cm.skysoft.app.dto.*;

import java.util.List;

/**
 * Created by francis on 2/10/21.
 */
public interface AfbExternalService {
    /**
     * Get All visit type.
     * @return List of VisitTypeDTO.
     */
    List<VisitTypeDTO> getListVisitTypeDtos();

    /**
     * Get All users.
     * @return List of UserAfbDTO.
     */
    List<UserAfbDTO> getListUsersDtos();

    /**
     * Get clients by userCode.
     * @return List of ClientDTO
     */
    List<ClientDTO> getListClientByUserCode();

    /**
     * Get clients by userCode.
     * @param userCode by user
     * @return List of ClientDTO
     */
    List<ClientDTO> getListClientByUserCode(String userCode);

    /**
     * Get All user supervisor by userCode.
     * @return List of UserAfbDTO
     */
    List<UserAfbDTO> getListUserSupervisorByUserCode();

    /**
     * @return userAfb connected.
     */
    AuthenticateUserAfbResponseDTO authenticateUserAfb(LoginUserAfbDTO loginUserAfbDTO);

    /**
     * @return userAfb connected.
     */
    List<UserAfbDTO> getAllUserSupervisorsByUSerCode(String userCode);

    /**
     * Get List client information category
     */
    List<ClientInformationCategoryDTO> getListClientInformationCategory();

    /**
     * get list products of AFB
     * @return list products
     */
    List<ProductDTO> getListProducts();

    /**
     * get list means used for the visit
     * @return list means used for the visit
     */
    List<MeansUsedForVisitDTO> getListMeansUsedForVisit();

    /**
     * @return userAfb connected.
     */
    List<UserAfbDTO> getAllUserSupervisedByUSerCode(String userCode);

    /**
     * @param clientCode by clientCode
     * @return info client for the prepa visit.
     */
    ClientPrepaInfosDTO getInfoPrepaVisit(String clientCode);

    /**
     * @param clientCode by clientCode
     * @return client by clientCode.
     */
    ClientDTO getClientByClientCode(String clientCode);

    /**
     * @param clientTransfertDTO by clientTransfertDTO
     * @return client by clientCode.
     */
    ClientTransfertResponseDTO clientTransfert(ClientTransfertDTO clientTransfertDTO);

    /**
     * @param clientCode by clientCode
     * @param categoryId by categoryId
     * @return list infosClient by categoryId and clientCode
     */
    ClientPrepaInfosDTO getClientByInfoCategory(String categoryId, String clientCode);

    /**
     * get type of commitment
     * @return list type of commitment
     */
    List<EngagementTypeDTO> getEngagementType();

    void saveUserAfb();

    List<User> getAllSupervised();

}
