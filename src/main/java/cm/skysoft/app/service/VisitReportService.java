package cm.skysoft.app.service;

import cm.skysoft.app.domain.VisitReport;
import cm.skysoft.app.domain.Visits;
import cm.skysoft.app.dto.VisitReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 4/26/21.
 */
public interface VisitReportService {

    /**
     * save visitReport.
     * @param visitReport the visitReport
     */
    VisitReport save(VisitReportDTO visitReport);

    /**
     * save visitReport for mobile.
     * @param visitReport the visitReport
     */
    VisitReport saveMobile(VisitReportDTO visitReport);

    /**
     * update visitReport.
     * @param visitReport the visitReport
     */
    VisitReport update(VisitReportDTO visitReport);

    /**
     * get visitReport by id.
     * @param id the id
     * @return visitReport found.
     */
    Optional<VisitReport> getVisitReportById(Long id);


    Optional<VisitReport> getVisitReportByVisitNoteVisitsVisitCode(String codeVisit);

    /**
     * get visitReport by id.
     * @param id the id
     * @return visitReport found.
     */
    VisitReport findVisitReportById(Long id);

    /**
     * get all VisitReports.
     * @param pageable the pageable
     * @return list of visitReport found.
     */
    Page<VisitReport> getAllVisitReports(Pageable pageable);

    /**
     * get all VisitReports.
     * @return list of visitReport found.
     */
    List<VisitReport> getAllVisitReports();

    /**
     * get all VisitReports by visitCode.
     * @param visitCode the visitCode
     * @return list of visitReport found.
     */
    List<VisitReport> getAllVisitReportsByVisitCode(String visitCode);

    /**
     * delete visitReport.
     * @param visitReportDTO the visitReport
     */
    void delete(VisitReportDTO visitReportDTO);

    Optional<Visits> updateVisitReportingStatus(Long id);

    /**
     * delete visitReport by Id.
     * @param id the id
     */
    void deleteById(Long id);
}
