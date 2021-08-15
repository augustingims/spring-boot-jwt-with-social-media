package cm.skysoft.app.web;

import cm.skysoft.app.service.JxlsManagerService;
import cm.skysoft.app.service.dto.ExcelReporting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by francis on 4/28/21.
 */
@RestController
@RequestMapping("/api")
public class JxlsManagerResource {

    private final Logger log = LoggerFactory.getLogger(JxlsManagerService.class);


    private final JxlsManagerService jxlsManagerService;

    public JxlsManagerResource(JxlsManagerService jxlsManagerService) {
        this.jxlsManagerService = jxlsManagerService;
    }


    @GetMapping("/generateVisitExcelReport/{visitCode}")
    public ResponseEntity<ExcelReporting> generateVisitExcelReport(@PathVariable String visitCode) throws IOException {

        log.debug("REST to get visit report excel file");
        ExcelReporting excelReporting = jxlsManagerService.generateVisitReport(visitCode);

        return new ResponseEntity<>(excelReporting, HttpStatus.OK);

    }
}
