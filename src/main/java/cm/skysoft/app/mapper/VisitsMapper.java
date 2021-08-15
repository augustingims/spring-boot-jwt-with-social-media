package cm.skysoft.app.mapper;

import org.springframework.stereotype.Service;

/**
 * Updated by daniel on 2/18/21.
 */

@Service
public class VisitsMapper {

//    @Autowired
//    private UserAfbsRepository userAfbsRepository;
//
//    @Autowired
//    private ClientsRepository clientsRepository;
//
//   public Visits visitDToTovisits(VisitDTO visitDTO){
//       if(visitDTO == null){
//           return null;
//       } else {
//           Visits visits = new Visits();
//           Clients clients = clientsRepository.findByIdClient(visitDTO.getClient().getId());
//           UserAfbs userAfbs = userAfbsRepository.findByIdUserAfb(visitDTO.getUser().getId());
//
//           visits.setVisitObject(visitDTO.getVisitObject());
//           visits.setVisitType(visitDTO.getVisitType());
//           visits.setClient(clients);
//           visits.setUserAfb(userAfbs);
//
//           return visits;
//       }
//   }
//
//   public VisitDTO visitsTovisitDTO(Visits visits){
//
//       VisitDTO visitDTO = new VisitDTO();
//       ClientDTO clientDTO = new ClientDTO();
//       UserAfbDTO userAfbDTO = new UserAfbDTO();
//
//       clientDTO.setId(visits.getClient().getIdClient());
//       userAfbDTO.setId(visits.getUserAfb().getIdUserAfb());
//
//       visitDTO.setVisitDate(visits.getVisitDate());
//       visitDTO.setVisitObject(visits.getVisitObject());
//       visitDTO.setVisitType(visits.getVisitType());
//       visitDTO.setId(visits.getId());
//       visitDTO.setClient(clientDTO);
//       visitDTO.setUser(userAfbDTO);
//
//       return visitDTO;
//   }
//
//   public List<Visits> visitDTOsTovisitsList(List<VisitDTO> visitDTOS){
//       if(visitDTOS == null){
//           return null;
//       }
//       List<Visits> visitsList = new ArrayList<Visits>(visitDTOS.size());
//       for(VisitDTO vi : visitDTOS){
//           visitsList.add(visitDToTovisits(vi));
//       }
//
//       return visitsList;
//   }
//
//    public List<VisitDTO> visitsListTovisitDTOs(List<Visits> visitsList){
//        if(visitsList == null){
//            return null;
//        }
//        List<VisitDTO> visitDTOList = new ArrayList<VisitDTO>(visitsList.size());
//        for(Visits vi : visitsList){
//            visitDTOList.add(visitsTovisitDTO(vi));
//        }
//
//        return visitDTOList;
//   }
//
//    public Visits visitssFromId(Long id){
//        if(id == null){
//            return null;
//        } else {
//            Visits visits = new Visits();
//            visits.setId(id);
//            return visits;
//        }
//    }

}
