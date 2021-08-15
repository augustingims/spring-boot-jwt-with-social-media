package cm.skysoft.app.service;

import cm.skysoft.app.domain.SuggestionParticipants;
import cm.skysoft.app.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SuggestionParticipantService {

    /**
     * save suggestionParticipants.
     * @param suggestionParticipants the suggestionParticipants
     * @return list of suggestionParticipants found.
     */
    SuggestionParticipants save(SuggestionParticipants suggestionParticipants);

    /**
     * update suggestionParticipants.
     * @param suggestionParticipants the suggestionParticipants
     */
    void update(SuggestionParticipants suggestionParticipants);

    /**
     * get suggestionParticipants by id.
     * @param id the id
     * @return suggestionParticipants found.
     */
    Optional<SuggestionParticipants> getsuggestionParticipantsById(Long id);

    /**
     * get suggestionParticipants by id.
     * @param idSuggestionParticipants the idSuggestionParticipants
     * @return suggestionParticipants found.
     */
    SuggestionParticipants findsuggestionParticipantsById(Long idSuggestionParticipants);

    /**
     * get all suggestionParticipants.
     * @param pageable the pageable
     * @return list of suggestionParticipants found.
     */
    Page<SuggestionParticipants> getsuggestionParticipants(Pageable pageable);

    /**
     * get all suggestionParticipants.
     * @return list of suggestionParticipants found.
     */
    List<SuggestionParticipants> getAllsuggestionParticipants();

    /**
     * get all UserParticipant.
     * @param idSuggestion the suggestion
     * @return list of UserDTO found.
     */
    List<UserDTO> getAllUserParticipants(Long idSuggestion);

    /**
     * delete suggestionParticipants.
     * @param suggestionParticipants the suggestionParticipants
     */
    void delete(SuggestionParticipants suggestionParticipants);

    /**
     * delete suggestionParticipants by Id.
     * @param id the id
     */
    void deleteById(Long id);
}
