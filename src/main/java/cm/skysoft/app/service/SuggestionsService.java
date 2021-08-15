package cm.skysoft.app.service;

import cm.skysoft.app.criteria.SuggestionCriteriaDTO;
import cm.skysoft.app.domain.Suggestions;
import cm.skysoft.app.domain.Visits;
import cm.skysoft.app.dto.SuggestionDTO;
import cm.skysoft.app.dto.SuggestionVisiteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by daniel on 2/18/21.
 */
public interface SuggestionsService {

    /**
     * save suggestionVisit.
     * @param suggestionVisiteDTO the suggestionVisit
     */
    Suggestions saveSuggestionVisit(SuggestionVisiteDTO suggestionVisiteDTO);

    /**
     * save saveSuggestionMobile.
     * @param suggestionVisiteDTO the saveSuggestionMobile
     */
    Suggestions saveSuggestionMobile(SuggestionVisiteDTO suggestionVisiteDTO);

    /**
     * update suggestion.
     * @param idSuggestion the suggestion
     */
    void update(Long idSuggestion);

    /**
     * get suggestion by id.
     * @param idSuggeston the idSuggeston
     * @return suggestion found.
     */
    Optional<Suggestions> getSuggestionsByIdSuggestions(Long idSuggeston);

    /**
     * get all Suggestions.
     * @param pageable the pageable
     * @return list of suggestion found.
     */
    Page<Suggestions> getAllSuggestions(Pageable pageable);

    /**
     * get all suggestion.
     * @return list of suggestion found.
     * @param dateDebut of suggestion
     * @param dateFin of suggestion
     * @param idClient of suggestion
     * @param idUserAfb of suggestion
     * @param resultmax of suggestion
     * @param status of suggestion
     */
    List<Suggestions> getAllSuggestionBySearch(Long idUserAfb, Long idUserAfbExpediteur, Long idClient, Long resultmax,
                                                 Boolean status, String dateDebut, String dateFin);


    /**
     * delete suggestion.
     * @param suggestionDTO the suggestion
     */
    void delete(SuggestionDTO suggestionDTO);

    /**
     * delete suggestion by Id.
     * @param id the id
     */
    void deleteById(Long id);

    /**
     * get number Suggestion Unread
     * @return the number of unread suggestions
     */
    Long getNumberSuggestionUnread();

    /**
     * find One Suggestion by codeSuggestion
     * @param codeSuggestion of the Suggestion
     */
    Suggestions findSuggestionByCodeSuggestion(String codeSuggestion);

    /**
     * getl all suggestion specification
     * @param suggestionCriteria the suggestionCriteria
     * @param pageable the pageable
     * @return all suggestion specification
     */
    Page<Suggestions> getAllSuggestionPage(SuggestionCriteriaDTO suggestionCriteria, Pageable pageable);

    /**
     * update visit by suggestion
     * @param suggestionVisiteDTO the suggestion
     * @return visit
     */
    Visits updateVisitBySuggestion(SuggestionVisiteDTO suggestionVisiteDTO);

    /**
     * not validates suggestion by visit
     * @param suggestionVisiteDTO the suggestion
     * @return visit
     */
    Suggestions notValidateSuggestionByVisit(SuggestionVisiteDTO suggestionVisiteDTO);

}
