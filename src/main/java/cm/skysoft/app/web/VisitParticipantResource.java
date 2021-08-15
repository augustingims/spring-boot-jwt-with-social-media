package cm.skysoft.app.web;

import cm.skysoft.app.domain.VisitParticipants;
import cm.skysoft.app.dto.VisitParticipantDTO;
import cm.skysoft.app.service.VisitParticipantsService;
import cm.skysoft.app.service.dto.UserDTO;
import cm.skysoft.app.utils.HeaderUtils;
import cm.skysoft.app.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * Created by francis on 3/4/21.
 */
@RestController
@RequestMapping("/api")
public class VisitParticipantResource {


    private final VisitParticipantsService visitParticipantsService;

    public static final String INDEFINI_TEXT = "-1";

    @Value("${application.clientApp.name}")
    private String applicationName;

    public VisitParticipantResource(VisitParticipantsService visitParticipantsService) {
        this.visitParticipantsService = visitParticipantsService;
    }

    /**
     * POST  /visitParticipant : save the visitParticipant on a visit.
     *
     * @param visitParticipant the suggestion View Model
     *
     */
    /*@PostMapping("/visitParticipant")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VisitParticipants> save(@RequestBody @Valid VisitParticipants visitParticipant) {
        VisitParticipants newVisitParticipants = visitParticipantsService.save(visitParticipant);
        return ResponseEntity.ok()
                .body(newVisitParticipants);
    }*/

    /**
     * PUT  /visitParticipant : update the visitParticipant.
     *
     * @param visitParticipant the suggestion View Model
     *
     */
    /*@PutMapping("/visitParticipant")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity updateAuthority(@Valid @RequestBody VisitParticipantDTO visitParticipant) {
        if (visitParticipant!= null && visitParticipant.getId()!=null)  {
            visitParticipantsService.update(visitParticipant);
            return ResponseEntity.ok().headers(HeaderUtils.createAlert(applicationName, "visitParticipant.updated", null)).build();
        }else {
            return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName,"visitParticipant.notupdated", null)).build();
        }
    }*/

    /**
     * GET  /visitParticipant : get all the visitParticipants.
     * @param pageable : the pageable
     * @return list of suggestions
     */
    @GetMapping("/visitParticipant")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<VisitParticipants>> getAllVisitParticipantsPage(Pageable pageable) {
        final Page<VisitParticipants> page = visitParticipantsService.getVisitParticipants(pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /visitParticipantUser : get all the visitParticipant User.
     * @param idVisit .
     * @return list of visitParticipant User found.
     */
    @GetMapping("/visitParticipantUser/{idVisit}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> getAllUserParticipants(@PathVariable Long idVisit) {
        List<UserDTO> userDTO = visitParticipantsService.getAllUserParticipants(idVisit);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * GET  /visitParticipant : get visitParticipant by id.
     * @param id : the Optional<VisitParticipants>
     * @return visitParticipant found.
     */
    @GetMapping("/visitParticipant/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VisitParticipants getVisitParticipantsById(@PathVariable Long id){
        return visitParticipantsService.getVisitParticipantById(id).orElse(null);
    }

    @DeleteMapping("/visitParticipant/{id}")
    public ResponseEntity<Void> deleteVisitById(@PathVariable Long id) {
        visitParticipantsService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName,"authority.deleted", null)).build();
    }

    @DeleteMapping("/visitParticipant")
    public ResponseEntity<Void> deleteVisit(@RequestBody VisitParticipantDTO visitParticipant) {
        visitParticipantsService.delete(visitParticipant);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert(applicationName, "authority.deleted", null)).build();
    }
}
