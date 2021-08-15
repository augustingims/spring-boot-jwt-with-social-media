package cm.skysoft.app.web;

import cm.skysoft.app.criteria.AgencyUserCriteria;
import cm.skysoft.app.criteria.UserCriteriaDTO;
import cm.skysoft.app.criteria.VisitCriteriaDTO;
import cm.skysoft.app.domain.User;
import cm.skysoft.app.domain.VisitNote;
import cm.skysoft.app.domain.Visits;
import cm.skysoft.app.dto.DashboardVisitDTO;
import cm.skysoft.app.dto.VisitDTO;
import cm.skysoft.app.dto.VisitNoteDTO;
import cm.skysoft.app.service.UserService;
import cm.skysoft.app.service.VisitNoteService;
import cm.skysoft.app.service.VisitsService;
import cm.skysoft.app.service.beans.DashboardVisitBeans;
import cm.skysoft.app.service.beans.DetailsVisitNoteBeans;
import cm.skysoft.app.service.dto.VisitNoteMobileDTO;
import cm.skysoft.app.service.impl.VisitsServiceImpl;
import cm.skysoft.app.utils.HeaderUtils;
import cm.skysoft.app.utils.MethoUtils;
import cm.skysoft.app.utils.PaginationUtils;
import cm.skysoft.app.web.exception.BadRequestAlertException;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by francis on 2/13/21.
 */
@RestController
@RequestMapping("/api")
public class VisitResource {

    private static final String instead  = "idexists";
    private final Logger log = LoggerFactory.getLogger(VisitResource.class);

    private final VisitsService visitsService;
    private final VisitNoteService visitNoteService;
    private final UserService userService;

    public VisitResource(VisitsServiceImpl visitsService, VisitNoteService visitNoteService, UserService userService) {

        this.visitsService = visitsService;
        this.visitNoteService = visitNoteService;
        this.userService = userService;
    }

    @Value("${application.clientApp.name}")
    private String applicationName;

    // private final Logger LOGGER = LoggerFactory.getLogger(VisitResource.class);



    /**
     * POST  /visits : save the visit.
     *
     * @param visit the visit View Model
     *
     */
    @PostMapping("/visits")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Visits> saveVisit(@RequestBody @Valid VisitDTO visit) {

        if (visit.getId()!=null)  {
            throw new BadRequestAlertException("The visit is already exists", "visits", instead);
        }else {

            Visits newVisit = visitsService.save(visit);
            return ResponseEntity.ok()
            .body(newVisit);
        }
    }

    /**
     * POST  /visitsPrepa : saveVisitPrepa the visit.
     *
     * @param visit the visit View Model
     * @param codeClient of client
     */
    @PostMapping("/visitsPrepa")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Visits> saveVisitPrepa(@RequestBody @Valid VisitDTO visit, @RequestParam(value = "codeClient", required = true) String codeClient) {

        if (visit.getId()!=null)  {
            throw new BadRequestAlertException("The visit is already exists", "visits", instead);
        }else {
            Visits newVisit = visitsService.saveVisitPrepa(visit, codeClient);
            return ResponseEntity.ok()
            .body(newVisit);
        }
    }

    /**
     * POST  /visitsPrepa : saveVisitPrepa the visit.
     *
     * @param visit the visit View Model
     * @param codeClient of client
     */
    @PostMapping("/visitsBureau")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Visits> saveVisitBureau(@RequestBody VisitDTO visit, @RequestParam(value = "codeClient", required = true) String codeClient) throws BadRequestException {

        if (visit.getId()!=null)  {
            throw new BadRequestAlertException("The visit is already exists", "visits", instead);
        }else {
            Visits newVisit = visitsService.saveVisitBureau(visit, codeClient);
            return ResponseEntity.ok()
            .body(newVisit);
        }
    }

    /**
     * PUT  /visits : update the visit.
     *
     * @param visit the visit View Model
     *
     */
    @PutMapping("/visits")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Visits> updateVisit(@Valid @RequestBody VisitDTO visit) {
        if (visit!= null && visit.getId()!=null)  {

            Visits visits = visitsService.update(visit);

            if(visits.isPlanification()) {
                return ResponseEntity.badRequest().body(visits);
            }
            return ResponseEntity.ok().body(visits);
        }else {
            return ResponseEntity.ok().body(new Visits());
        }
    }

    @PutMapping("/update-visit-note")
    public ResponseEntity<VisitNote> updateVsiitNote(@Valid @RequestBody VisitNoteDTO visitNoteDTO) {
        VisitNote v = visitNoteService.update(visitNoteDTO);
        if(v.getVisits().isExecution()){
            return ResponseEntity.badRequest().body(v);
        }
        return ResponseEntity.ok().body(v);
    }

    /**
     * GET  /visits : get all the visits.
     * @param pageable : the pageable
     * @return list of visits
     */
    @GetMapping("/visits")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Visits>> getAllVisitsPage(Pageable pageable) {
        final Page<Visits> page = visitsService.getAllVisits(pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /visitsAll : get all the visits.
     * @return list of visits
     */
    @GetMapping("/visitsAll")
    public ResponseEntity<List<Visits>> getAllVisits() {
        final List<Visits> page = visitsService.getAllVisits();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

  /**
     * GET  /visitsAll : get all the visits.
     * @return list of visits by visitDate
     */
    @GetMapping("/listByVisitDate")
    public ResponseEntity<List<Visits>> getAllVisitsByVisitDate(@RequestParam(value = "idClient", required = false) Long idClient,
                                                @RequestParam(value = "idUserAfb", required = false) Long idUserAfb,
                                                @RequestParam(value = "dateDebut", required = false) String dateDebut,
                                                @RequestParam(value = "dateFin", required = false) String dateFin,
                                                @RequestParam(value = "moyenUtilise", required = false) String moyenUtilise,
                                                @RequestParam(value = "typeVisit", required = false) String typeVisit, Pageable pageable) {

        VisitCriteriaDTO visitCriterai = new VisitCriteriaDTO();

        typeVisit = ("Tous".equals(typeVisit) || typeVisit == null) ? null
                : typeVisit;

        moyenUtilise = ("Tous".equals(moyenUtilise) || moyenUtilise == null) ? null
                : moyenUtilise;

        // User user = userService.getCurrentUser();

        visitCriterai.setVisitType(typeVisit);
        visitCriterai.setIdCustomer(idClient);
        visitCriterai.setDateBefore(dateFin);
        visitCriterai.setDateAfter(dateDebut);
        visitCriterai.setMeansUsed(moyenUtilise);
        visitCriterai.setIdUser(idUserAfb);
        visitCriterai.setExecution(Boolean.FALSE);

        Page<Visits> visitsPage = visitsService.getAllVisitPreparate(visitCriterai, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),visitsPage);
        return new ResponseEntity<>(visitsPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /getAllVisitSpecification : get all the visits specification.
     * @return list of visits by specification
     */
    @GetMapping("/getAllVisitSpecification")
    public ResponseEntity<List<Visits>> getAllVisitSpecification(@RequestParam(value = "idClient", required = false) Long idClient,
                                                                @RequestParam(value = "dateDebut", required = false) String dateDebut,
                                                                @RequestParam(value = "dateFin", required = false) String dateFin,
                                                                @RequestParam(value = "moyenUtilise", required = false) String moyenUtilise,
                                                                @RequestParam(value = "typeVisit", required = false) String typeVisit, Pageable pageable) {

        VisitCriteriaDTO visitCriterai = new VisitCriteriaDTO();

        typeVisit = ("Tous".equals(typeVisit) || typeVisit == null) ? null
                : typeVisit;

        moyenUtilise = ("Tous".equals(moyenUtilise) || moyenUtilise == null) ? null
                : moyenUtilise;

        User user = userService.getCurrentUser();

        visitCriterai.setVisitType(typeVisit);
        visitCriterai.setMeansUsed(moyenUtilise);
        visitCriterai.setIdCustomer(idClient);
        visitCriterai.setDateBefore(dateFin);
        visitCriterai.setDateAfter(dateDebut);
        visitCriterai.setIdUser(user.getId());
        visitCriterai.setExecution(Boolean.FALSE);
        visitCriterai.setPreparation(Boolean.FALSE);
        visitCriterai.setPlanification(Boolean.FALSE);

        Page<Visits> visitsPage = visitsService.getAllVisit(visitCriterai, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),visitsPage);
        return new ResponseEntity<>(visitsPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /getAllVisitSpecification : get all the visits specification.
     * @return list of visits by specifications
     */
    @GetMapping("/getAllVisitToExecuted")
    public ResponseEntity<List<Visits>> getAllVisitToExecuted(@RequestParam(value = "idClient", required = false) Long idClient,
                                                                @RequestParam(value = "dateDebut", required = false) String dateDebut,
                                                                @RequestParam(value = "dateFin", required = false) String dateFin,
                                                                @RequestParam(value = "moyenUtilise", required = false) String moyenUtilise,
                                                                @RequestParam(value = "typeVisit", required = false) String typeVisit, Pageable pageable) {

        VisitCriteriaDTO visitCriterai = new VisitCriteriaDTO();

        typeVisit = ("Tous".equals(typeVisit) || typeVisit == null) ? null
                : typeVisit;

        moyenUtilise = ("Tous".equals(moyenUtilise) || moyenUtilise == null) ? null
                : moyenUtilise;

        User user = userService.getCurrentUser();

        visitCriterai.setVisitType(typeVisit);
        visitCriterai.setMeansUsed(moyenUtilise);
        visitCriterai.setIdCustomer(idClient);
        visitCriterai.setDateBefore(dateFin);
        visitCriterai.setDateAfter(dateDebut);
        visitCriterai.setIdUser(user.getId());
        visitCriterai.setExecution(Boolean.FALSE);
        visitCriterai.setPreparation(Boolean.TRUE);

        Page<Visits> visitsPage = visitsService.getAllVisit(visitCriterai, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),visitsPage);
        return new ResponseEntity<>(visitsPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /getAllVisitSpecification : get all the visits specification.
     * @return list of visits by specification user create visit
     */
    @GetMapping("/getListVisitCreatedForPreparation")
    public ResponseEntity<List<Visits>> getAllVisitPreparedSpecification(@RequestParam(value = "idClient", required = false) Long idClient,
                                                                         @RequestParam(value = "dateDebut", required = false) String dateDebut,
                                                                         @RequestParam(value = "dateFin", required = false) String dateFin,
                                                                         @RequestParam(value = "typeVisit", required = false) String typeVisit, Pageable pageable) {

        VisitCriteriaDTO visitCriterai = new VisitCriteriaDTO();

        typeVisit = ("Tous".equals(typeVisit) || typeVisit == null) ? null
                : typeVisit;

        User user = userService.getCurrentUser();

        visitCriterai.setVisitType(typeVisit);
        visitCriterai.setIdCustomer(idClient);
        visitCriterai.setDateBefore(dateFin);
        visitCriterai.setDateAfter(dateDebut);
        visitCriterai.setIdUser(user.getId());
        visitCriterai.setExecution(Boolean.FALSE);
        visitCriterai.setPreparation(Boolean.FALSE);
        visitCriterai.setPlanification(Boolean.TRUE);

        Page<Visits> visitsPage = visitsService.getAllVisit(visitCriterai, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),visitsPage);
        return new ResponseEntity<>(visitsPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /getAllVisitSpecification : get all the visits specification.
     * @return list of visits by specification
     */
    @GetMapping("/getListVisitForPreparation")
    public ResponseEntity<List<Visits>> getListVisitForPreparation(@RequestParam(value = "idClient", required = false) Long idClient,
                                                                   @RequestParam(value = "dateDebut", required = false) String dateDebutText,
                                                                   @RequestParam(value = "dateFin", required = false) String dateFinText,
                                                                   @RequestParam(value = "typeVisit", required = false) String typeVisit, Pageable pageable){

        LocalDateTime dateDebut = null;
        LocalDateTime dateFin = null;
        if(!dateDebutText.isEmpty()){
            dateDebut = MethoUtils.convertorDateWithoutFormat(dateDebutText);
        } else if (!dateFinText.isEmpty()){
            dateFin = MethoUtils.convertorDateWithoutFormat(dateFinText);
        }

        typeVisit = ("Tous".equals(typeVisit) || typeVisit == null) ? null
                : typeVisit;
        Page<Visits> visitsPage = visitsService.getListVisitByParticipant(idClient, typeVisit, dateDebut, dateFin, Boolean.FALSE, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),visitsPage);
        return new ResponseEntity<>(visitsPage.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/visits/{id}")
    public Visits getVisitById(@PathVariable Long id) {
        return visitsService.getVisitById(id).orElse(null);
    }

    @DeleteMapping("/visits/{id}")
    public ResponseEntity<Void> deleteVisitById(@PathVariable Long id) {
        visitsService.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert(applicationName, "authority.deleted", null)).build();
    }

   /* @DeleteMapping("/visits")
    public ResponseEntity<Void> deleteVisit(@RequestBody Visits visit) {
        visitsService.delete(visit);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName,"authority.deleted", null)).build();
    }*/

    @GetMapping("/getNumberVisitToPreparate")
    public ResponseEntity<Long> getNumberVisitToPreparate(){
        User user = userService.getCurrentUser();

        Long numberVisitToPreparate= visitsService.getNumberVisitToPreparate(user);

        if (numberVisitToPreparate == null){
            numberVisitToPreparate = (long) 0;
        }
        return new ResponseEntity<>(numberVisitToPreparate, HttpStatus.OK);
    }

    @GetMapping("/getNumberVisitPlanned")
    public ResponseEntity<Long> getNumberVisitPlanned(){
        User user = userService.getCurrentUser();

        Long numberVisitToPlanned= visitsService.getNumberVisitToPlanned(user);

        if (numberVisitToPlanned == null){
            numberVisitToPlanned = (long) 0;
        }
        return new ResponseEntity<>(numberVisitToPlanned, HttpStatus.OK);
    }

    @GetMapping("/getNumberVisitCreatedToPreparate")
    public ResponseEntity<Long> getNumberVisitCreatedToPreparate(){
        User user = userService.getCurrentUser();

        Long numberVisitCreatedToPreparate= visitsService.getNumberVisitCreatedToPreparate(user);

        if (numberVisitCreatedToPreparate == null){
            numberVisitCreatedToPreparate = (long) 0;
        }
        return new ResponseEntity<>(numberVisitCreatedToPreparate, HttpStatus.OK);
    }

    @GetMapping("/getNumberVisitForExecuted")
    public ResponseEntity<Long> getNumberVisitForExecuted(){
        User user = userService.getCurrentUser();

        Long numberVisitForExecuted= visitsService.getNumberVisitForExecuted(user);

        if (numberVisitForExecuted == null){
            numberVisitForExecuted = (long) 0;
        }
        System.out.println("number execution : " + numberVisitForExecuted);
        return new ResponseEntity<>(numberVisitForExecuted, HttpStatus.OK);
    }

    @GetMapping("/getNumberVisiteToExcecuted")
    public ResponseEntity<Long> getNumberVisiteToExcecuted(){
        User user = userService.getCurrentUser();

        Long numberVisitCreatedToExecuted= visitsService.getNumberVisiteToExcecuted(user);

        if (numberVisitCreatedToExecuted == null){
            numberVisitCreatedToExecuted = (long) 0;
        }
        return new ResponseEntity<>(numberVisitCreatedToExecuted, HttpStatus.OK);
    }

    /**
     * {@code GET /visits/:codeVisit} : get the "codeVisit" visits.
     *
     * @param codeVisit the codeVisit of the visits to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "codeVisit" visits, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/visitByCode/{codeVisit}")
    public ResponseEntity<Visits> getVisitByCodeVisit(@PathVariable String codeVisit) {
        log.debug("REST request to get User : {}", codeVisit);
        return new ResponseEntity<>(
                visitsService.findVisitByCodeVisit(codeVisit), HttpStatus.OK);
    }

    /**
     * POST /saveVisitNote : save VisitNote
     * @param visitNote the visitNote
     * @return new visitNote
     */
    @PostMapping("/saveVisitNote")
    public ResponseEntity<VisitNote> saveVisitNote(@RequestBody VisitNoteDTO visitNote){
        if (visitNote.getId()!=null)  {
            throw new BadRequestAlertException("The visit note is already exists", "visitNote", "idexists");
        }else {
            VisitNote newVisit = visitNoteService.save(visitNote);
            return ResponseEntity.ok()
                    .body(newVisit);
        }
    }

    /**
     * POST /saveVisitNote : save VisitNote
     * @param visitNote the visitNote
     * @return new visitNote
     */
    @PostMapping("/saveVisitNoteMobile")
    public ResponseEntity<VisitNote> saveVisitNote(@RequestPart("file") List<MultipartFile> file, @RequestPart("visitNote") VisitNoteMobileDTO visitNote) throws IOException {
        if (visitNote.getId()!=null)  {
            throw new BadRequestAlertException("The visit note is already exists", "visitNote", "idexists");
        }else {
            VisitNote newVisit = visitNoteService.saveVisitNoteMobile(file, visitNote);
            return ResponseEntity.ok()
                    .body(newVisit);
        }
    }

    /**
     * POST /updateVisitNote : update VisitNote
     * @param visitNote the visitNote
     * @return new visitNote
     */
    @PostMapping("/updateVisitNoteMobile")
    public ResponseEntity<VisitNote> updateVisitNote(@RequestPart("file") List<MultipartFile> file, @RequestPart("visitNote") VisitNoteMobileDTO visitNote) throws IOException {
        VisitNote newVisit = visitNoteService.updateVisitNoteMobile(file, visitNote);
        return ResponseEntity.ok().body(newVisit);

    }

    /**
     * GET  /getAllVisitToReporting : get all the visits executed for the reporting.
     * @return list of visits executed for the reporting
     */
    @GetMapping("/getAllVisitToReporting")
    public ResponseEntity<List<Visits>> getAllVisitToReporting(@RequestParam(value = "idClient", required = false) Long idClient,
                                                               @RequestParam(value = "dateDebut", required = false) String dateDebut,
                                                               @RequestParam(value = "dateFin", required = false) String dateFin,
                                                               @RequestParam(value = "moyenUtilise", required = false) String moyenUtilise,
                                                               @RequestParam(value = "typeVisit", required = false) String typeVisit, Pageable pageable) {

        VisitCriteriaDTO visitCriterai = new VisitCriteriaDTO();

        typeVisit = ("Tous".equals(typeVisit) || typeVisit == null) ? null : typeVisit;
        moyenUtilise = ("Tous".equals(moyenUtilise) || moyenUtilise == null) ? null : moyenUtilise;

        User user = userService.getCurrentUser();

        visitCriterai.setVisitType(typeVisit);
        visitCriterai.setMeansUsed(moyenUtilise);
        visitCriterai.setIdCustomer(idClient);
        visitCriterai.setDateBefore(dateFin);
        visitCriterai.setDateAfter(dateDebut);
        visitCriterai.setIdUser(user.getId());
        visitCriterai.setExecution(Boolean.TRUE);
        visitCriterai.setReporting(Boolean.FALSE);

        Page<Visits> visitsPage = visitsService.getAllVisit(visitCriterai, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),visitsPage);
        return new ResponseEntity<>(visitsPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /getAllVisitToReporting : get all the visits executed for the reporting.
     * @return list of visits executed for the reporting
     */
    @GetMapping("/getAllVisitToArchivate")
    public ResponseEntity<List<Visits>> getAllVisitToArchivate(@RequestParam(value = "idClient", required = false) Long idClient,
                                                               @RequestParam(value = "dateDebut", required = false) String dateDebut,
                                                               @RequestParam(value = "dateFin", required = false) String dateFin,
                                                               @RequestParam(value = "moyenUtilise", required = false) String moyenUtilise,
                                                               @RequestParam(value = "typeVisit", required = false) String typeVisit, Pageable pageable) {

        VisitCriteriaDTO visitCriterai = new VisitCriteriaDTO();

        typeVisit = ("Tous".equals(typeVisit) || typeVisit == null) ? null : typeVisit;
        moyenUtilise = ("Tous".equals(moyenUtilise) || moyenUtilise == null) ? null : moyenUtilise;

        User user = userService.getCurrentUser();

        visitCriterai.setVisitType(typeVisit);
        visitCriterai.setMeansUsed(moyenUtilise);
        visitCriterai.setIdCustomer(idClient);
        visitCriterai.setDateBefore(dateFin);
        visitCriterai.setDateAfter(dateDebut);
        visitCriterai.setIdUser(user.getId());
        visitCriterai.setReporting(Boolean.TRUE);
        visitCriterai.setArchivage(Boolean.FALSE);

        Page<Visits> visitsPage = visitsService.getAllVisit(visitCriterai, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),visitsPage);
        return new ResponseEntity<>(visitsPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /getAllVisitToReporting : get all the visits executed for the reporting.
     * @return list of visits executed for the reporting
     */
    @GetMapping("/findAllVisitToArchivate")
    public ResponseEntity<List<Visits>> findAllVisitToArchivate(@RequestParam(value = "codeClient", required = false) String codeClient,
                                                               @RequestParam(value = "dateDebut", required = false) String dateDebut,
                                                               @RequestParam(value = "dateFin", required = false) String dateFin,
                                                               @RequestParam(value = "moyenUtilise", required = false) String moyenUtilise,
                                                               @RequestParam(value = "typeVisit", required = false) String typeVisit, Pageable pageable) {

        VisitCriteriaDTO visitCriterai = new VisitCriteriaDTO();

        typeVisit = ("Tous".equals(typeVisit) || typeVisit == null) ? null : typeVisit;
        moyenUtilise = ("Tous".equals(moyenUtilise) || moyenUtilise == null) ? null : moyenUtilise;

        List<Visits> v = Collections.emptyList();
        Page<Visits> visitsPage = new PageImpl<>(v);

        visitCriterai.setVisitType(typeVisit);
        visitCriterai.setMeansUsed(moyenUtilise);
        visitCriterai.setCodeClient(codeClient);
        visitCriterai.setDateBefore(dateFin);
        visitCriterai.setDateAfter(dateDebut);
        visitCriterai.setReporting(Boolean.TRUE);
        visitCriterai.setArchivage(Boolean.FALSE);

        if(visitCriterai.getCodeClient() != null) {
            visitsPage = visitsService.getAllVisit(visitCriterai, pageable);
        }
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),visitsPage);
        return new ResponseEntity<>(visitsPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /getAllVisitToReporting : get all the visits executed for the reporting.
     * @return list of visits executed for the reporting
     */
    @GetMapping("/getAllVisitForSupervised")
    public ResponseEntity<List<Visits>> getAllVisitForSupervised(@RequestParam(value = "idClient", required = false) Long idClient,
                                                                 @RequestParam(value = "idUserAfb", required = false) Long idUserAfb,
                                                                 @RequestParam(value = "dateDebut", required = false) String dateDebut,
                                                                 @RequestParam(value = "dateFin", required = false) String dateFin,
                                                                 @RequestParam(value = "moyenUtilise", required = false) String moyenUtilise,
                                                                 @RequestParam(value = "planification", required = false) Boolean planification,
                                                                 @RequestParam(value = "preparation", required = false) Boolean preparation,
                                                                 @RequestParam(value = "execution", required = false) Boolean execution,
                                                                 @RequestParam(value = "reporting", required = false) Boolean reporting,
                                                                 @RequestParam(value = "archivage", required = false) Boolean archivage,
                                                                 @RequestParam(value = "typeVisit", required = false) String typeVisit,
                                                                 Pageable pageable) {

        VisitCriteriaDTO visitCriterai = new VisitCriteriaDTO();

        typeVisit = ("Tous".equals(typeVisit) || typeVisit == null) ? null : typeVisit;
        moyenUtilise = ("Tous".equals(moyenUtilise) || moyenUtilise == null) ? null : moyenUtilise;

        visitCriterai.setVisitType(typeVisit);
        visitCriterai.setMeansUsed(moyenUtilise);
        visitCriterai.setIdCustomer(idClient);
        visitCriterai.setIdUser(idUserAfb);
        visitCriterai.setDateBefore(dateFin);
        visitCriterai.setDateAfter(dateDebut);
        visitCriterai.setPlanification(planification);
        visitCriterai.setPreparation(preparation);
        visitCriterai.setExecution(execution);
        visitCriterai.setReporting(reporting);
        visitCriterai.setArchivage(archivage);

        System.out.println("\n\n\n\n" + visitCriterai + "\n\n\n\n");

        Page<Visits> visitsPage = visitsService.getAllVisit(visitCriterai, pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),visitsPage);
        return new ResponseEntity<>(visitsPage.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/detailVisitNote/{visitCode}")
    public ResponseEntity<DetailsVisitNoteBeans> detailVisiteNote(@PathVariable String visitCode) {
        return ResponseEntity.ok(visitNoteService.detailVisitNote(visitCode));
    }

    @GetMapping("/update-status-preparation/{idVisit}")
    public ResponseEntity updateStatusPreparetion(@PathVariable Long idVisit) {
        visitsService.updatePreparationStatus(idVisit);
        return ResponseEntity.ok().body(idVisit);
    }

    @GetMapping("/validate-preparation/{idVisit}")
    public ResponseEntity<Visits> validatePreparation(@PathVariable Long idVisit) {
        Visits v = visitsService.validateVisit(idVisit);
        return ResponseEntity.ok(visitsService.validateVisit(idVisit));
    }

    @GetMapping("/validate-visit-note/{codeVisitNote}")
    public ResponseEntity<VisitNote> validateVisitNote(@PathVariable String codeVisitNote) {
        return ResponseEntity.ok(visitNoteService.validatedVisitNote(codeVisitNote));
    }

    @GetMapping("/getVisitNoteByVisitCode/{visitCode}")
    public ResponseEntity<VisitNote> getVisitNoteByVisitCode(@PathVariable String visitCode) {
        return ResponseEntity.ok(visitNoteService.getVisitNoteByVisitCode(visitCode));
    }

    @GetMapping("/getVisitNoteByCodeVisit/{visitCode}")
    public ResponseEntity<DetailsVisitNoteBeans> getVisitNoteByCodeVisit(@PathVariable String visitCode) {
        return ResponseEntity.ok(visitNoteService.getVisitNoteByVisitsCode(visitCode));
    }

    @GetMapping("/getVisitNoteByVisit/{visitCode}")
    public ResponseEntity<DetailsVisitNoteBeans> getVisitNoteByVisit(@PathVariable String visitCode) {
        return ResponseEntity.ok(visitNoteService.getvisitnotebyvisitsCode(visitCode));
    }

    @GetMapping("/countVisitNoteByCurrentYear")
    public Long countVisitNoteByCurrentYear(@RequestParam("codeClient") String codeClient) {
        return visitNoteService.countVisitNoteByCurrentYear(codeClient);
    }

    @GetMapping("/getLastVisit")
    public VisitNote getLastVisit(@RequestParam("codeClient") String codeClient) {
        return visitNoteService.getLastVisitNote(codeClient);
    }

    @GetMapping("/dashboardVisit")
    public ResponseEntity<DashboardVisitDTO> dashboardVisit(@RequestParam(value = "dateBefore", required = false) String dateBefore,
                                                            @RequestParam(value = "dateAfter", required = false) String dateAfter,
                                                            @RequestParam("periode") String periode){
        return ResponseEntity.ok().body(visitsService.getNumberTotalVisit(dateBefore, dateAfter, periode));
    }

    @GetMapping("/dashboardVisitForUserSupervised")
    public ResponseEntity<List<DashboardVisitBeans>> dashboardVisitForUserSupervised(@RequestParam(value = "dateBefore", required = false) String dateBefore,
                                                                                     @RequestParam(value = "dateAfter", required = false) String dateAfter,
                                                                                     @RequestParam(value = "periode", required = false) String periode,
                                                                                     @RequestParam(value = "userCode", required = false) String userCode,
                                                                                     @RequestParam(value = "login", required = false) String login,
                                                                                     @RequestParam(value = "profilUser", required = false) String profil,
                                                                                     @RequestParam(value = "idAgencyRegion", required = false) String idAgencyRegion,
                                                                                     @RequestParam(value = "idAgency", required = false) String idAgency){

        AgencyUserCriteria agencyUserCriteria = new AgencyUserCriteria();

        userCode = (userCode == null || userCode.equals("") || userCode.equals("null")) ? null : userCode;
        login = (login == null || login.equals("") || login.equals("null")) ? null : login;
        idAgency = (idAgency == null || idAgency.equals("") || idAgency.equals("null")) ? null : idAgency;
        idAgencyRegion = (idAgencyRegion == null || idAgencyRegion.equals("") || idAgencyRegion.equals("null")) ? null : idAgencyRegion;
        profil = (profil == null || profil.equals("") || profil.equals("null")) ? null : profil;

        if(userCode != null) {
            agencyUserCriteria.setUserCode(userCode);
        }
        if(login != null){
            agencyUserCriteria.setLogin(login);
        }
        if(profil != null){
            agencyUserCriteria.setProfil(profil);
        }
        if(idAgency != null){
            agencyUserCriteria.setIdAgency(Long.valueOf(idAgency));
        }
        if(idAgencyRegion != null){
            agencyUserCriteria.setIdAgencyRegion(Long.valueOf(idAgencyRegion));
        }
        return ResponseEntity.ok().body(visitsService.getNumberTotalVisitForUserSupervised(dateBefore, dateAfter, periode, agencyUserCriteria));
    }

    @GetMapping("/findNumberTotalVisitForUserSupervised")
    public ResponseEntity<DashboardVisitBeans> findNumberTotalVisitForUserSupervised(@RequestParam(value = "dateBefore", required = false) String dateBefore,
                                                                                     @RequestParam(value = "dateAfter", required = false) String dateAfter,
                                                                                     @RequestParam(value = "periode", required = false) String periode,
                                                                                     @RequestParam(value = "userCode", required = false) String userCode,
                                                                                     @RequestParam(value = "login", required = false) String login,
                                                                                     @RequestParam(value = "idAgency", required = false) String idAgency){

        UserCriteriaDTO userCriteriaDTO = new UserCriteriaDTO();

        userCode = (userCode == null || userCode.equals("") || userCode.equals("null")) ? null : userCode;
        login = (login == null || login.equals("") || login.equals("null")) ? null : login;
        idAgency = (idAgency == null || idAgency.equals("") || idAgency.equals("null")) ? null : idAgency;

        if(userCode != null) {
            userCriteriaDTO.setUserCode(userCode);
        }
        if(login != null){
            userCriteriaDTO.setLogin(login);
        }
        if(idAgency != null){
            userCriteriaDTO.setIdAgency(Long.valueOf(idAgency));
        }
        return ResponseEntity.ok().body(visitsService.getTotalVisitForUserSupervised(dateBefore, dateAfter, periode, userCriteriaDTO));
    }

}
