package cm.skysoft.app.mapper;

import cm.skysoft.app.domain.Agency;
import cm.skysoft.app.domain.Clients;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.dto.AgencyDTO;
import cm.skysoft.app.dto.ClientDTO;
import cm.skysoft.app.dto.UserAfbDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 2/17/21.
 */
/*@Mapper(componentModel = "spring", uses = {UserAfbMapper.class, AgencyMapper.class},
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)*/
@Service
public class ClientsMapper {

  public Clients clientsDTOToclients(ClientDTO clientDTO){
      Clients clients = new Clients();
      User userAfbs = new User();
      Agency agency = new Agency();

      userAfbs.setIdUserAfb(clientDTO.getManager().getId());

      agency.setIdAgency(clientDTO.getAgency().getId());


      clients.setIdClient(clientDTO.getId());
      clients.setActivityProfile(clientDTO.getActivityProfile());
      clients.setBirthDate(clientDTO.getBirthDate());
      clients.setBirthPlace(clientDTO.getBirthPlace());
      clients.setCodeClient(clientDTO.getCodeClient());
      clients.setEmail(clientDTO.getEmail());
      clients.setEntryDate(clientDTO.getEntryDate());
      clients.setFirstName(clientDTO.getFirstName());
      clients.setLastName(clientDTO.getLastName());
      clients.setLegalForm(clientDTO.getLegalForm());
      clients.setParticularProfile(clientDTO.getParticularProfile());
      clients.setPhoneNumber(clientDTO.getPhoneNumber());
      clients.setManager(userAfbs);
      clients.setAgency(agency);

      return clients;
  }

  private ClientDTO clientsToClientDTO(Clients clients){

      ClientDTO clientDTO = new ClientDTO();
      UserAfbDTO userAfbDTO = new UserAfbDTO();
      AgencyDTO agencyDTO = new AgencyDTO();

      agencyDTO.setId(clients.getAgency().getIdAgency());

      userAfbDTO.setId(clients.getManager().getIdUserAfb());

      clientDTO.setLegalForm(clients.getLegalForm());
      clientDTO.setLastName(clients.getLastName());
      clientDTO.setFirstName(clients.getFirstName());
      clientDTO.setEntryDate(clients.getEntryDate());
      clientDTO.setEmail(clients.getEmail());
      clientDTO.setCodeClient(clients.getCodeClient());
      clientDTO.setBirthPlace(clients.getBirthPlace());
      clientDTO.setBirthDate(clients.getBirthDate());
      clientDTO.setActivityProfile(clients.getActivityProfile());
      clientDTO.setId(clients.getIdClient());
      clientDTO.setAgency(agencyDTO);
      clientDTO.setManager(userAfbDTO);

      return new ClientDTO();
  }


  public List<Clients> clientDTOsToClients(List<ClientDTO> clientDTOS){
      ArrayList<Clients> clientsList = new ArrayList<>(clientDTOS.size());
      for(ClientDTO cl : clientDTOS){
          clientsList.add(clientsDTOToclients(cl));
      }

      return clientsList;
  }

  public List<ClientDTO> clientsToClientDTOs(List<Clients> clients){
     ArrayList<ClientDTO> clientDTOList = new ArrayList<>(clients.size());
     for(Clients cl : clients){
         clientDTOList.add(clientsToClientDTO(cl));
     }

     return clientDTOList;
  }

  public Clients clientsFromId(Long id){
      Clients clients = new Clients();
      clients.setId(id);
      return clients;
  }

}
