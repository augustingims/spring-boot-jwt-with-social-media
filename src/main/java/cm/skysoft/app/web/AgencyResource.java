package cm.skysoft.app.web;

import cm.skysoft.app.domain.Agency;
import cm.skysoft.app.domain.AgencyUser;
import cm.skysoft.app.dto.AgencyRegionDTO;
import cm.skysoft.app.service.AgencyRegionService;
import cm.skysoft.app.service.AgencyService;
import cm.skysoft.app.service.AgencyUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Created by francis on 2/13/21.
 */
@RestController
@RequestMapping("/api")
public class AgencyResource {

    private final AgencyService agencyService;
    private final AgencyUserService agencyUserService;
    private final AgencyRegionService agencyRegionService;

    public AgencyResource(AgencyService agencyService, AgencyUserService agencyUserService, AgencyRegionService agencyRegionService) {
        this.agencyService = agencyService;
        this.agencyUserService = agencyUserService;
        this.agencyRegionService = agencyRegionService;
    }

    @GetMapping("/findAllAgency")
    public ResponseEntity<List<Agency>> findAllAgency(){
        return ResponseEntity.ok().body(agencyService.findAll());
    }

    @GetMapping("/findAllAgencyRegion")
    public ResponseEntity<List<AgencyRegionDTO>> findAllAgencyRegion(){
        return ResponseEntity.ok().body(agencyRegionService.getAllAgencyRegions());
    }

    @GetMapping("/findAgencyUserByIdUser/{idUser}")
    public ResponseEntity<Optional<AgencyUser>> findAgencyUserByIdUser(@PathVariable Long idUser){
        return ResponseEntity.ok().body(agencyUserService.getAgencyUserByIdUser(idUser));
    }
}
