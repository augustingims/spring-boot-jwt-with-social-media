package cm.skysoft.app.web;

import cm.skysoft.app.domain.VisitType;
import cm.skysoft.app.service.VisitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Daniel the 01/03/2021
 */

@RestController
@RequestMapping("/api")
public class VisitTypeResource {

    @Autowired
    VisitTypeService visitTypeService;

    @GetMapping("/findAllVisitType")
    public List<VisitType> findAllVisitType(){
        return visitTypeService.findAll();
    }

}
