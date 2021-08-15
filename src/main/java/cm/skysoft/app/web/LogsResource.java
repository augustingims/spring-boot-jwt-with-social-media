package cm.skysoft.app.web;

import cm.skysoft.app.criteria.LogsCriteria;
import cm.skysoft.app.domain.Logs;
import cm.skysoft.app.service.LogsService;
import cm.skysoft.app.utils.MethoUtils;
import cm.skysoft.app.utils.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by francis on 5/28/21.
 */
@RestController
@RequestMapping("/api")
public class LogsResource {

    private final LogsService logsService;

    public LogsResource(LogsService logsService) {
        this.logsService = logsService;
    }


    @RequestMapping(value = "/mouchard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Logs> mouchard(
            @RequestParam(value = "agence", required = false) Long agence,
            @RequestParam(value = "debut", required = false) String debut,
            @RequestParam(value = "fin", required = false) String fin,
            @RequestParam(value = "libelle", required = false) String libelle,
            @RequestParam(value = "resultatmax", required = false) Long resultatmax) {

        List<Logs> logs;

        LocalDateTime datedebut = MethoUtils.convertorDateTime00H00(debut);
        LocalDateTime datefin = MethoUtils.convertorDateTime24H00(fin);

        logs = logsService.getMouchard(agence, datedebut, datefin, libelle,
                resultatmax);

        return logs;
    }

    @GetMapping("/logs")
    public ResponseEntity<List<Logs>> getLogs(
            @RequestParam(value = "dateBefore", required = false) String dateBefore,
            @RequestParam(value = "dateAfter", required = false) String dateAfter,
            @RequestParam(value = "login", required = false) String login,
            Pageable pageable) {

        LogsCriteria logsCriteria = new LogsCriteria();

        login = (login == null || login.equals("") || login.equals("null")) ? null : login;
        dateAfter = (dateAfter == null || dateAfter.equals("") || dateAfter.equals("null")) ? null : dateAfter;
        dateBefore = (dateBefore == null || dateBefore.equals("") || dateBefore.equals("null")) ? null : dateBefore;

        if(login != null && !login.isEmpty()) {
            logsCriteria.setLogin(login);
        }
        if(dateBefore != null && !dateBefore.isEmpty()) {
            logsCriteria.setDateBefore(dateBefore);
        }
        if(dateAfter != null && !dateAfter.isEmpty()) {
            logsCriteria.setDateAfter(dateAfter);
        }

        Page<Logs> page = logsService.findAll(pageable, logsCriteria);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
