package cm.skysoft.app.web;

import cm.skysoft.app.criteria.SuggestionCriteriaDTO;
import cm.skysoft.app.domain.Suggestions;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.domain.Visits;
import cm.skysoft.app.dto.SuggestionVisiteDTO;
import cm.skysoft.app.service.SuggestionsService;
import cm.skysoft.app.service.UserService;
import cm.skysoft.app.utils.PaginationUtils;
import cm.skysoft.app.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by daniel on 2/18/21.
 */
@RestController
@RequestMapping("/api")
public class SuggestionResource {


    private final SuggestionsService suggestionsService;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(VisitResource.class);

    @Value("${application.clientApp.name}")
    private String applicationName;

    public SuggestionResource(SuggestionsService suggestionsService, UserService userService) {
        this.suggestionsService = suggestionsService;
        this.userService = userService;
    }

    /**
     * POST  /suggestions : save the suggestion on a visit.
     *
     * @param suggestionVisiteDTO the suggestion View Model
     *
     */
    @PostMapping("/suggestionsVisite")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Suggestions> saveSuggestion(@RequestBody @Valid SuggestionVisiteDTO suggestionVisiteDTO) {
        Suggestions newVisit = suggestionsService.saveSuggestionVisit(suggestionVisiteDTO);
        return ResponseEntity.ok()
                .body(newVisit);
    }

    /**
     * POST  /suggestions : save the suggestion on a visit.
     *
     * @param suggestionVisiteDTO the suggestion View Model
     *
     */
    @PostMapping("/suggestionsVisite/mobile")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Suggestions> saveSuggestionMobile(@RequestBody @Valid SuggestionVisiteDTO suggestionVisiteDTO) {
        Suggestions newVisit = suggestionsService.saveSuggestionMobile(suggestionVisiteDTO);
        return ResponseEntity.ok()
                .body(newVisit);
    }

    /**
     * GET  /notificationsAllBySendTo : get all the notifications.
     * @return list of notifications
     */
    @GetMapping("/numberSuggestionUnreadByUser")
    public ResponseEntity<Long> getNumberSuggestionUnread() {
        Long numberNotification = suggestionsService.getNumberSuggestionUnread();

        if (numberNotification == null){
            numberNotification = (long) 0;
        }
        return new ResponseEntity<>(numberNotification, HttpStatus.OK);
    }

    /*@PostMapping("/suggestionsSpontannee")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Suggestions> saveSuggestionSpontanne(@RequestBody @Valid SuggestionDTO suggestionDTO) {
        Suggestions newVisit = suggestionsService.saveSuggestionSpontanee(suggestionDTO);
        return ResponseEntity.ok()
                .body(newVisit);
    }*/

    /**
     * PUT  /suggestions : update the suggestion on a visit.
     *
     * @param idSuggestion the suggestion View Model
     *
     */
    @GetMapping("/update-suggestions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity updateSuggestion(@RequestParam("idSuggestion") Long idSuggestion) {
        if (idSuggestion != null)  {
            suggestionsService.update(idSuggestion);
            return ResponseEntity.ok().body(idSuggestion);
        }else {
            return ResponseEntity.ok().body(idSuggestion);
        }
    }

    /**
     * GET  /suggestions : get all the suggestions.
     * @param pageable : the pageable
     * @return list of suggestions
     */
    @GetMapping("/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Suggestions>> getAllSuggestionPage(Pageable pageable) {
        final Page<Suggestions> page = suggestionsService.getAllSuggestions(pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /suggestions : get all the suggestions.
     * @param idClient : the list
     * @return list of suggestions
     */
    @GetMapping("/listSuggestions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Suggestions>> getAllSuggestionBySearch(@RequestParam(value = "idClient", required = false) Long idClient,
                                                                      @RequestParam(value = "idUserAfb", required = false) Long idUserAfb,
                                                                      @RequestParam(value = "status", required = false) String etat,
                                                                      @RequestParam(value = "dateTimeDebut", required = false)String dateDebut,
                                                                      @RequestParam(value = "dateTimeFin", required = false) String dateFin,
                                                                      @RequestParam(value = "resultmax", required = false) Long resultmax,
                                                                      @RequestParam(value = "idUserAfbExpediteur", required = false) Long idUserAfbExpediteur) {

        Boolean status = ("Tous".equals(etat) || etat == null) ? null
                : ("1".equals(etat) ? Boolean.TRUE : Boolean.FALSE);


        String login = SecurityUtils.getCurrentUserLogin().orElse(null);


        User user = userService.getUserWithAuthoritiesByLogin(login).orElse(null);

        if(user != null) {
            if (SecurityUtils.isCurrentUserInRole("ROLE_GFC")) {
                idUserAfb = user.getId();
            } else {
                idUserAfbExpediteur = user.getId();
            }
        }

        final List<Suggestions> suggestionsList = suggestionsService.getAllSuggestionBySearch(idUserAfb, idUserAfbExpediteur, idClient, resultmax, status, dateDebut, dateFin);
        return new ResponseEntity<>(suggestionsList, HttpStatus.OK);
    }

    /**
     * GET  /suggestions : get all the suggestions.
     * @param idClient : the list
     * @return list of suggestions
     */
    @GetMapping("/getAllSuggestionSpecification")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Suggestions>> getAllSuggestionSpecification(@RequestParam(value = "idClient", required = false) Long idClient,
                                                                      @RequestParam(value = "idUserAfb", required = false) Long idUserAfb,
                                                                      @RequestParam(value = "status", required = false) String etat,
                                                                      @RequestParam(value = "dateTimeDebut", required = false)String dateDebut,
                                                                      @RequestParam(value = "dateTimeFin", required = false) String dateFin,
                                                                           @RequestParam(value = "validated", required = false) String validated,
                                                                           @RequestParam(value = "notValidated", required = false) String notValidated,
                                                                           Pageable pageable) {

        SuggestionCriteriaDTO suggestionCriteria = new SuggestionCriteriaDTO();

       User user = userService.getCurrentUser();

        Boolean status = ("Tous".equals(etat) || etat == null) ? null
                : ("1".equals(etat) ? Boolean.TRUE : Boolean.FALSE);

        Boolean validatedBoolean = ("Tous".equals(validated) || validated == null) ? null
                : ("1".equals(validated) ? Boolean.TRUE : Boolean.FALSE);

        Boolean notvalidatedBoolean = ("Tous".equals(notValidated) || notValidated == null) ? null
                : ("1".equals(notValidated) ? Boolean.TRUE : Boolean.FALSE);

        suggestionCriteria.setDateAfter(dateDebut);
        suggestionCriteria.setDateBefore(dateFin);
        suggestionCriteria.setIdCustomer(idClient);
        suggestionCriteria.setStatus(status);
        suggestionCriteria.setIdUserSender(user.getId());
        suggestionCriteria.setNotValidated(notvalidatedBoolean);
        suggestionCriteria.setValidated(validatedBoolean);

        Page<Suggestions> suggestionsPage = suggestionsService.getAllSuggestionPage(suggestionCriteria, pageable);

        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), suggestionsPage);
        return new ResponseEntity<>(suggestionsPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /getAllSuggestionRecueSpecification : get all the suggestions reçue.
     * @param idClient : the list
     * @return list of suggestions reçue
     */
    @GetMapping("/getAllSuggestionRecueSpecification")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Suggestions>> getAllSuggestionRecueSpecification(@RequestParam(value = "idClient", required = false) Long idClient,
                                                                      @RequestParam(value = "idUserAfb", required = false) Long idUserAfb,
                                                                      @RequestParam(value = "status", required = false) String etat,
                                                                      @RequestParam(value = "dateTimeDebut", required = false)String dateDebut,
                                                                      @RequestParam(value = "dateTimeFin", required = false) String dateFin,
                                                                      @RequestParam(value = "idUserAfbExpediteur", required = false) Long idUserAfbExpediteur,
                                                                      @RequestParam(value = "validated", required = false) String validated,
                                                                           Pageable pageable) {

        SuggestionCriteriaDTO suggestionCriteria = new SuggestionCriteriaDTO();

        String login = SecurityUtils.getCurrentUserLogin().orElse(null);

        if(login != null) {
            User user = userService.getUserWithAuthoritiesByLogin(login).orElse(null);
            if (user != null) {
                idUserAfb = user.getId();
            }
        }

        Boolean status = ("Tous".equals(etat) || etat == null) ? null
                : ("1".equals(etat) ? Boolean.TRUE : Boolean.FALSE);

        suggestionCriteria.setDateAfter(dateDebut);
        suggestionCriteria.setDateBefore(dateFin);
        suggestionCriteria.setIdCustomer(idClient);
        suggestionCriteria.setStatus(status);
        suggestionCriteria.setIdUserAfb(idUserAfb);
        suggestionCriteria.setValidated(Boolean.FALSE);
        suggestionCriteria.setNotValidated(Boolean.FALSE);

        Page<Suggestions> suggestionsPage = suggestionsService.getAllSuggestionPage(suggestionCriteria, pageable);

        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), suggestionsPage);
        return new ResponseEntity<>(suggestionsPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /suggestions : get all the suggestions.
     * @param idSuggestions : the Optional<Suggestions>
     * @return list of suggestions
     */
    @GetMapping("/suggestions/{idSuggestions}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Suggestions> getSuggestionsByIdSuggestions(@PathVariable Long idSuggestions){
        return suggestionsService.getSuggestionsByIdSuggestions(idSuggestions);
    }

    /**
     * {@code GET /suggestionByCode/:codeSuggestion} : get the "codeSuggestion" suggestion.
     *
     * @param codeSuggestion the codeSuggestion of the suggestion to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "codeSuggestion" suggestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/suggestionByCode/{codeSuggestion}")
    public ResponseEntity<Suggestions> getVisitByCodeVisit(@PathVariable String codeSuggestion) {
        log.debug("REST request to get User : {}", codeSuggestion);
        return new ResponseEntity<>(
                suggestionsService.findSuggestionByCodeSuggestion(codeSuggestion), HttpStatus.OK);
    }

    @PutMapping("/updateVisitBySuggestion")
    public ResponseEntity<Visits> updateVisitBySuggestion(@RequestBody SuggestionVisiteDTO suggestionVisiteDTO){
        Visits newVisit = suggestionsService.updateVisitBySuggestion(suggestionVisiteDTO);
        return ResponseEntity.ok()
                .body(newVisit);
    }

    @PutMapping("/notValidateSuggestionByVisit")
    public ResponseEntity<Suggestions> notValidateSuggestionByVisit(@RequestBody SuggestionVisiteDTO suggestionVisiteDTO){
        Suggestions s = suggestionsService.notValidateSuggestionByVisit(suggestionVisiteDTO);
        return ResponseEntity.ok()
                .body(s);
    }
}
