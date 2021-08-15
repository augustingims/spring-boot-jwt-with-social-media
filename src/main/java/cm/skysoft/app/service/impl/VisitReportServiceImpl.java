package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.VisitNote;
import cm.skysoft.app.domain.VisitReport;
import cm.skysoft.app.domain.Visits;
import cm.skysoft.app.dto.VisitReportDTO;
import cm.skysoft.app.repository.VisitReportRepository;
import cm.skysoft.app.repository.VisitsRepository;
import cm.skysoft.app.service.VisitNoteService;
import cm.skysoft.app.service.VisitReportService;
import cm.skysoft.app.service.VisitsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 4/26/21.
 */
@Service
@Transactional
public class VisitReportServiceImpl implements VisitReportService {

    private final VisitReportRepository visitReportRepository;
    private final VisitsRepository visitsRepository;
    private final VisitNoteService visitNoteService;
    private final VisitsService visitsService;

    public VisitReportServiceImpl(VisitReportRepository visitReportRepository, VisitsRepository visitsRepository, VisitNoteService visitNoteService, VisitsService visitsService) {
        this.visitReportRepository = visitReportRepository;
        this.visitsRepository = visitsRepository;
        this.visitNoteService = visitNoteService;
        this.visitsService = visitsService;
    }


    /**
     * save visitReport.
     *
     * @param visitReport the visitReport
     */
    @Override
    public VisitReport save(VisitReportDTO visitReport) {
        Visits visits;
        VisitReport v = null;
        VisitNote visitNote = null;

        if (visitReport.getVisitCode() != null) {
            visits = visitsService.findVisitByCodeVisit(visitReport.getVisitCode());

            if (visits != null) {

                if (!visits.isGenerateReport()) {
                    visits.setGenerateReport(Boolean.TRUE);
                    visitsRepository.save(visits);
                }

                visitNote = visitNoteService.findVisitNoteByidVisit(visits.getId()).orElse(null);
            }
        }

        if (visitReport.getId() == null) {
            if (visitNote != null) {
                v = new VisitReport(null, visitReport.getSummaryOfExchange(),
                        visitReport.getResolution(), visitReport.getSubjectExchanged(),
                        visitReport.getExecutionDelay(), LocalDateTime.now(), visitNote );
            }
        } else {
            throw new RuntimeException();
        }
        return visitReportRepository.save(v);
    }

    @Override
    public VisitReport saveMobile(VisitReportDTO visitReport) {
        Visits visits;
        VisitReport v = null;
        VisitNote visitNote = null;

        if (visitReport.getVisitCode() != null) {
            visits = visitsService.findVisitByCodeVisit(visitReport.getVisitCode());

            if (visits != null) {

                visits.setGenerateReport(Boolean.TRUE);
                visits.setReporting(Boolean.TRUE);
                visitsRepository.save(visits);

                visitNote = visitNoteService.findVisitNoteByidVisit(visits.getId()).orElse(null);
            }
        }

        if (visitReport.getId() == null) {
            if (visitNote != null) {
                v = new VisitReport(null, visitReport.getSummaryOfExchange(),
                        visitReport.getResolution(), visitReport.getSubjectExchanged(),
                        visitReport.getExecutionDelay(), LocalDateTime.now(), visitNote );
            }
        } else {
            throw new RuntimeException();
        }
        return visitReportRepository.save(v);
    }

    /**
     * update visitReport.
     *
     * @param visitReport the visitReport
     */
    @Override
    public VisitReport update(VisitReportDTO visitReport) {
        VisitReport report = null;

        Optional<VisitReport> visitReportToUpdate = visitReportRepository.findById(visitReport.getId());

        if(visitReportToUpdate.isPresent()) {
            visitReportToUpdate.get().setResolution(visitReport.getResolution());
            visitReportToUpdate.get().setSubjectExchanged(visitReport.getSubjectExchanged());
            visitReportToUpdate.get().setExecutionDelay(visitReport.getExecutionDelay());
            visitReportToUpdate.get().setSummaryOfExchange(visitReport.getSummaryOfExchange());

            report = visitReportRepository.save(visitReportToUpdate.get());
        }

        return report;
    }

    /**
     * update visit.
     *
     * @param id the visitReport
     */
    @Override
    public Optional<Visits> updateVisitReportingStatus(Long id) {

        Optional<Visits> v;
        Optional<Visits> visit = Optional.empty();

        v = visitsRepository.findById(id);

        if (v.isPresent() && !v.get().isReporting()) {
            visit = v;
            v.get().setReporting(Boolean.TRUE);
            visitsRepository.save(visit.get());
        }



        return visit;
    }

    /**
     * get visitReport by id.
     *
     * @param id the id
     * @return visitReport found.
     */
    @Override
    public Optional<VisitReport> getVisitReportById(Long id) {
        return visitReportRepository.findById(id);
    }

    @Override
    public Optional<VisitReport> getVisitReportByVisitNoteVisitsVisitCode(String codeVisit) {
        return visitReportRepository.getVisitReportByVisitNoteVisitsVisitCode(codeVisit);
    }

    /**
     * get visitReport by id.
     *
     * @param id the id
     * @return visitReport found.
     */
    @Override
    public VisitReport findVisitReportById(Long id) {
        return visitReportRepository.getOne(id);
    }

    /**
     * get all VisitReports.
     *
     * @param pageable the pageable
     * @return list of visitReport found.
     */
    @Override
    public Page<VisitReport> getAllVisitReports(Pageable pageable) {
        return visitReportRepository.findAll(pageable);
    }

    /**
     * get all VisitReports.
     *
     * @return list of visitReport found.
     */
    @Override
    public List<VisitReport> getAllVisitReports() {
        return visitReportRepository.findAll();
    }

    /**
     * get all VisitReports by visitCode.
     *
     * @param visitCode the visitCode
     * @return list of visitReport found.
     */
    @Override
    public List<VisitReport> getAllVisitReportsByVisitCode(String visitCode) {
        return visitReportRepository.getVisitReportsByVisitCode(visitCode);
    }

    /**
     * delete visitReport.
     *
     * @param visitReportDTO the visitReport
     */
    @Override
    public void delete(VisitReportDTO visitReportDTO) {
        VisitReport visitReport = findVisitReportById(visitReportDTO.getId());
        if(visitReport != null) {
            visitReportRepository.delete(visitReport);
        }
    }

    /**
     * delete visitReport by Id.
     *
     * @param id the id
     */
    @Override
    public void deleteById(Long id) {

        Optional <VisitReport> report = visitReportRepository.findById(id);

        visitReportRepository.deleteById(id);

        if (report.isPresent()) {
            Visits visit = report.get().getVisitNote().getVisits();
           boolean vt = visitReportRepository.getVisitReportsByVisitCode(visit.getVisitCode()).isEmpty();

            if (vt) {
                visit.setGenerateReport(Boolean.FALSE);
                visitsRepository.save(visit);
            }
        }
    }
}
