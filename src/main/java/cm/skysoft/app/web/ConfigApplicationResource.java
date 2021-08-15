package cm.skysoft.app.web;

import cm.skysoft.app.domain.ConfigApplication;
import cm.skysoft.app.domain.ConfigSms;
import cm.skysoft.app.domain.ConfigVisit;
import cm.skysoft.app.dto.ConfigApplicationDTO;
import cm.skysoft.app.dto.ConfigSmsDTO;
import cm.skysoft.app.dto.ConfigVisitDTO;
import cm.skysoft.app.service.ConfigApplicationService;
import cm.skysoft.app.service.ConfigSmsService;
import cm.skysoft.app.service.ConfigVisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/configApplication")
public class ConfigApplicationResource {

    private final ConfigApplicationService configApplicationService;
    private final ConfigVisitService configVisitService;
    private final ConfigSmsService configSmsService;

    public ConfigApplicationResource(ConfigApplicationService configApplicationService, ConfigVisitService configVisitService, ConfigSmsService configSmsService) {
        this.configApplicationService = configApplicationService;
        this.configVisitService = configVisitService;
        this.configSmsService = configSmsService;
    }

    @PutMapping("/update")
    public ResponseEntity<ConfigApplication> update(@Valid @RequestBody ConfigApplicationDTO configApplicationDTO) {
        ConfigApplication configApplication = configApplicationService.update(configApplicationDTO);

        return ResponseEntity.ok().body(configApplication);
    }

    @GetMapping("/{code}")
    public ConfigApplication findByCode(@PathVariable String code) {
        return configApplicationService.findByCode(code).orElse(null);
    }

    @PostMapping("/visit")
    public ConfigVisit saveConfigVisit(@RequestBody ConfigVisitDTO configVisitDTO) throws IOException {
        return configVisitService.save(configVisitDTO);
    }

    @GetMapping("/visit")
    public ConfigVisit getConfigVisit() {
        return configVisitService.getOne();
    }


    @GetMapping("/findAllConfigPattern")
    public ResponseEntity<ConfigApplication> findAllConfigPattern() {
        return ResponseEntity.ok().body(configApplicationService.findOne());
    }

    @PostMapping("/configSms")
    public ConfigSms saveConfigSms(@RequestBody ConfigSmsDTO configSmsDTO) throws IOException {
        return configSmsService.save(configSmsDTO);
    }

    @GetMapping("/configSms")
    public ConfigSms getConfigSms() {
        return configSmsService.getOne();

    }

//    @GetMapping("/visit")
//    public ConfigVisitDTO getConfigVisit() throws IOException {
//        ConfigVisitDTO configVisitDTO = null;
//        try {
//
//            File file = ResourceUtils.getFile("classpath:config-file/config-file.csv");
//
//            CSVReader reader = new CSVReader(new FileReader(file), ';', '"', 1);
//
//            //Read single row at once
//            String[] firstRow = reader.readNext();
//
//            if (firstRow.length > 0) {
//                configVisitDTO = new ConfigVisitDTO();
//                configVisitDTO.setTypeVisit(firstRow[0]);
//                configVisitDTO.setDureeMoyen(Integer.valueOf(firstRow[1]));
//                configVisitDTO.setObjetVisit(firstRow[2]);
//
//                System.out.println("\n\n\n\n\n\n\n " +configVisitDTO.getTypeVisit() +" "+ configVisitDTO.getDureeMoyen() +" "+ configVisitDTO.getObjetVisit() +" \n\n\n\n\n\n\n ");
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return configVisitDTO;
//    }
}
