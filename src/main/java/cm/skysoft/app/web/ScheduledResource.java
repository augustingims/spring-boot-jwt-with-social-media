package cm.skysoft.app.web;

import cm.skysoft.app.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ScheduledResource {


    private final VisitTypeService visitTypeService;
    private final AfbExternalService afbExternalService;
    private final CategoryInfoClientService categoryInfoClientService;
    private final MeansUsedForVisitService meansUsedForVisitService;
    private final ProductsService productsService;
    private final EngagementTypeService engagementTypeService;
    private final VisitsService visitsService;
    private final ConfigApplicationService configApplicationService;
    private final VisitNoteService visitNoteService;

    public ScheduledResource(VisitTypeService visitTypeService, AfbExternalService afbExternalService, CategoryInfoClientService categoryInfoClientService, MeansUsedForVisitService meansUsedForVisitService, ProductsService productsService, EngagementTypeService engagementTypeService, VisitsService visitsService, ConfigApplicationService configApplicationService, VisitNoteService visitNoteService) {
        this.visitTypeService = visitTypeService;
        this.afbExternalService = afbExternalService;
        this.categoryInfoClientService = categoryInfoClientService;
        this.meansUsedForVisitService = meansUsedForVisitService;
        this.productsService = productsService;
        this.engagementTypeService = engagementTypeService;
        this.visitsService = visitsService;
        this.configApplicationService = configApplicationService;
        this.visitNoteService = visitNoteService;
    }


    @Scheduled(cron = "0 02 02 * * *")
    public void saveDataAfb(){
        visitTypeService.save();
        categoryInfoClientService.save();
        productsService.save();
        meansUsedForVisitService.save();
        engagementTypeService.save();
        visitsService.getBeetwenDateVisit();
        visitNoteService.getBeetwenDateVisitNoteCreated();
        afbExternalService.saveUserAfb();
    }

    @GetMapping("/synchronizationDataToAfb")
    public ResponseEntity<Void> synchronizationDataToAfb(){
        visitTypeService.save();
        categoryInfoClientService.save();
        productsService.save();
        meansUsedForVisitService.save();
        engagementTypeService.save();
        configApplicationService.save();
        afbExternalService.saveUserAfb();

        return ResponseEntity.ok().build();
    }
}
