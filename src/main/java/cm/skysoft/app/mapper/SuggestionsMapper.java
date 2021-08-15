package cm.skysoft.app.mapper;

import cm.skysoft.app.domain.Suggestions;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.domain.Visits;
import cm.skysoft.app.dto.SuggestionDTO;
import cm.skysoft.app.dto.UserAfbDTO;
import cm.skysoft.app.dto.VisitDTO;
import cm.skysoft.app.repository.VisitsRepository;
import cm.skysoft.app.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 2/17/21.
 */
@Service
public class SuggestionsMapper {

    private final VisitsRepository visitsRepository;

    private final UserService userService;

    public SuggestionsMapper(VisitsRepository visitsRepository, UserService userService) {
        this.visitsRepository = visitsRepository;
        this.userService = userService;
    }

    public Suggestions suggestionDToTosuggestions(SuggestionDTO suggestionDTO){
        Suggestions suggestions = new Suggestions();
        User user = userService.getCurrentUser();
        Visits visits = visitsRepository.findVisitsById(suggestionDTO.getVisit().getId());

        suggestions.setMotivation(suggestionDTO.getMotivation());
        suggestions.setUserAfb(user);
        suggestions.setVisit(visits);
        suggestions.setClients(visits.getClient());
        suggestions.setId(suggestionDTO.getId());
        suggestions.setUserAfbExpediteur(visits.getUserAfb());
        suggestions.setStatus(suggestionDTO.isStatus());
        return suggestions;
    }

    private SuggestionDTO suggestionTosuggestionDTO(Suggestions suggestions){
        SuggestionDTO suggestionDTO = new SuggestionDTO();
        UserAfbDTO userAfbDTO = new UserAfbDTO();
        VisitDTO visitDTO = new VisitDTO();

        userAfbDTO.setId(suggestions.getUserAfb().getIdUserAfb());

        visitDTO.setId(suggestions.getVisit().getId());

        suggestionDTO.setId(suggestions.getId());
        suggestionDTO.setMotivation(suggestions.getMotivation());
        suggestionDTO.setVisit(visitDTO);
        suggestionDTO.setUser(userAfbDTO);

        return suggestionDTO;
    }

    public List<Suggestions> suggestionDTOsTosuggestionsList(List<SuggestionDTO> suggestionDTOS){
        List<Suggestions> suggestionsList = new ArrayList<>( suggestionDTOS.size() );
        for ( SuggestionDTO suggestionDTO : suggestionDTOS ) {
            suggestionsList.add(suggestionDToTosuggestions(suggestionDTO) );
        }

        return suggestionsList;
    }

    public List<SuggestionDTO> suggestionsListTosuggestionDTOs(List<Suggestions> suggestionsList){
        List<SuggestionDTO> suggestionDTOList = new ArrayList<>( suggestionsList.size() );
        for ( Suggestions suggestions : suggestionsList ) {
            suggestionDTOList.add(suggestionTosuggestionDTO(suggestions) );
        }

        return suggestionDTOList;
    }

    public Suggestions suggestionsFromId(Long id){
        Suggestions suggestions = new Suggestions();
        suggestions.setId(id);
        return suggestions;
    }
}
