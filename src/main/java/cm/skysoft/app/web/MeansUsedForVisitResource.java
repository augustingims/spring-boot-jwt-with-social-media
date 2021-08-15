package cm.skysoft.app.web;

import cm.skysoft.app.domain.MeansUsedForVisit;
import cm.skysoft.app.service.MeansUsedForVisitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/meansUsedForVisit")
public class MeansUsedForVisitResource {

    private final MeansUsedForVisitService meansUsedForVisitService;

    public MeansUsedForVisitResource(MeansUsedForVisitService meansUsedForVisitService) {
        this.meansUsedForVisitService = meansUsedForVisitService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<MeansUsedForVisit>> findAll(){
        List<MeansUsedForVisit> meansUsedForVisitList = meansUsedForVisitService.findAll();
        return new ResponseEntity<>(meansUsedForVisitList, HttpStatus.OK);
    }
}
