package cm.skysoft.app.web;

import cm.skysoft.app.domain.EngagementType;
import cm.skysoft.app.service.EngagementTypeService;
import cm.skysoft.app.service.EngagementTypeVisitNoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/engagementType")
public class EngagementTypeResource {

    private final EngagementTypeService engagementTypeService;
    private final EngagementTypeVisitNoteService engagementTypeVisitNoteServiceService;

    public EngagementTypeResource(EngagementTypeService engagementTypeService, EngagementTypeVisitNoteService engagementTypeVisitNoteServiceService) {
        this.engagementTypeService = engagementTypeService;
        this.engagementTypeVisitNoteServiceService = engagementTypeVisitNoteServiceService;
    }

    @GetMapping
    public ResponseEntity<List<EngagementType>> findAll(){
        List<EngagementType> engagementTypeList = engagementTypeService.findAll();
        return new ResponseEntity<>(engagementTypeList, HttpStatus.OK);
    }

    @GetMapping("/{visitNoteId}")
    public ResponseEntity<List<EngagementType>> findByIdVisitNote(@PathVariable Long visitNoteId){
        List<EngagementType> engagementTypeList = engagementTypeVisitNoteServiceService.findEngagementTypeByVisitNoteId(visitNoteId);
        return new ResponseEntity<>(engagementTypeList, HttpStatus.OK);
    }
}
