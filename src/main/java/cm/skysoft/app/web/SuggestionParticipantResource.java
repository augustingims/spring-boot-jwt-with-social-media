package cm.skysoft.app.web;

import cm.skysoft.app.service.SuggestionParticipantService;
import cm.skysoft.app.service.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SuggestionParticipantResource {

    private  final SuggestionParticipantService suggestionParticipantService;

    public SuggestionParticipantResource(SuggestionParticipantService suggestionParticipantService) {
        this.suggestionParticipantService = suggestionParticipantService;
    }

    /**
     * GET  /suggestionParticipantUser : get all the suggestionParticipant User.
     * @param idSuggestion .
     * @return list of suggestionParticipant User found.
     */
    @GetMapping("/suggestionParticipantUser/{idSuggestion}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> getAllUserParticipants(@PathVariable Long idSuggestion) {
        List<UserDTO> userDTO = suggestionParticipantService.getAllUserParticipants(idSuggestion);
        return ResponseEntity.ok(userDTO);
    }
}
