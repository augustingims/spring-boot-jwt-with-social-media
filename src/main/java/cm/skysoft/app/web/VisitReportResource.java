package cm.skysoft.app.web;

import cm.skysoft.app.domain.VisitReport;
import cm.skysoft.app.domain.Visits;
import cm.skysoft.app.dto.VisitReportDTO;
import cm.skysoft.app.service.VisitReportService;
import cm.skysoft.app.utils.HeaderUtils;
import cm.skysoft.app.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 4/26/21.
 */
@Controller
@RequestMapping("/api")
@Transactional
public class VisitReportResource {

    private final VisitReportService visitReportService;

    @Value("${application.clientApp.name}")
    private String applicationName;

    public VisitReportResource(VisitReportService visitReportService) {
        this.visitReportService = visitReportService;
    }

    /**
     * POST  /visitReport : save the visitReport on a visitReport.
     *
     * @param visitReport the visitReport View Model
     *
     */
    @PostMapping("/visitReport")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VisitReport> saveVisitReport(@RequestBody @Valid VisitReportDTO visitReport) {
        VisitReport newVisitReport = visitReportService.save(visitReport);
        if(newVisitReport.getVisitNote().getVisits().isReporting()){
            return ResponseEntity.badRequest().body(newVisitReport);
        }
        return ResponseEntity.ok().body(newVisitReport);
    }

    /**
     * POST  /visitReport : save the visitReport on a visitReport for mobile.
     *
     * @param visitReport the visitReport View Model
     *
     */
    @PostMapping("/visitReportMobile")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VisitReport> saveVisitReportMobile(@RequestBody @Valid VisitReportDTO visitReport) {
        VisitReport newVisitReport = visitReportService.saveMobile(visitReport);
        return ResponseEntity.ok().body(newVisitReport);
    }

    /**
     * PUT  /visitReport : update the visitReport.
     *
     * @param visitReport the suggestion View Model
     *
     */
    @PutMapping("/visitReport")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity updateVisitReport(@Valid @RequestBody VisitReportDTO visitReport) {
        if (visitReport!= null && visitReport.getId()!=null)  {
            VisitReport report = visitReportService.update(visitReport);
            return ResponseEntity.ok().body(report);
        }else {
            return ResponseEntity.ok().headers(HeaderUtils.createAlert( applicationName, "visitReport.notupdated", null)).build();
        }
    }

    /**
     * GET  /visitReport : get all the visitReports.
     * @param pageable : the pageable
     * @return list of visitReport
     */
    @GetMapping("/visitReport")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<VisitReport>> getAllVisitReportPage(Pageable pageable) {
        final Page<VisitReport> page = visitReportService.getAllVisitReports(pageable);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /visitReport : get all the visitReports by visitCode.
     * @param visitCode : the List<VisitReport>
     * @return list of visitReport
     */
    @GetMapping("/visitReportsByVisitCode/{visitCode}")
    public ResponseEntity<List<VisitReport>> getAllVisitReportsByVisitCode(@PathVariable String visitCode) {
        final List<VisitReport> visitReports = visitReportService.getAllVisitReportsByVisitCode(visitCode);
        return new ResponseEntity<>(visitReports, HttpStatus.OK);
    }

    @GetMapping("/visitReportsByVisitNote_Visits_Code/{visitCode}")
    public ResponseEntity<Optional<VisitReport>> visitReportsByVisitNote_Visits_Code(@PathVariable String visitCode) {
        return new ResponseEntity<>(visitReportService.getVisitReportByVisitNoteVisitsVisitCode(visitCode), HttpStatus.OK);
    }

    /**
     * GET  /visitReport : get visitReport by id.
     * @param id : the Optional<visitReport>
     * @return visitParticipant found.
     */
    @GetMapping("/visitReport/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VisitReport getVisitReportById(@PathVariable Long id){
        return visitReportService.getVisitReportById(id).orElse(null);
    }

    @DeleteMapping("/visitReport/{id}")
    public ResponseEntity deleteVisitReportById(@PathVariable Long id) {
        visitReportService.deleteById(id);
        return new ResponseEntity<>("Rapport de visite supprimer avec succès", HttpStatus.OK);
    }

    @GetMapping("/visitReportVisit/{id}")
    public ResponseEntity updateVisitReportingStatus(@PathVariable Long id) {
        Optional<Visits> visit = visitReportService.updateVisitReportingStatus(id);

        if (visit.isPresent()) {
            return new ResponseEntity<>("Rapport de visite valider avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cette visite est déjà validée", HttpStatus.OK);
        }
    }

    @DeleteMapping("/visitReport")
    public ResponseEntity<Void> deleteVisitReport(@RequestBody VisitReportDTO visitReport) {
        visitReportService.delete(visitReport);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert(applicationName, "visitReport.deleted", null)).build();
    }
}
