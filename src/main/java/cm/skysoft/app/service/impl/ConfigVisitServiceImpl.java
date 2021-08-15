package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.ConfigVisit;
import cm.skysoft.app.dto.ConfigVisitDTO;
import cm.skysoft.app.repository.ConfigVisitRepository;
import cm.skysoft.app.service.ConfigVisitService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by francis on 6/23/21.
 */
@Service
public class ConfigVisitServiceImpl implements ConfigVisitService {

    private final ConfigVisitRepository configVisitRepository;

    public ConfigVisitServiceImpl(ConfigVisitRepository configVisitRepository) {
        this.configVisitRepository = configVisitRepository;
    }

    @Override
    public ConfigVisit save(ConfigVisitDTO configVisitDTO) {

        ConfigVisit configVisit = new ConfigVisit();
        configVisit.setId(configVisitDTO.getId());
        configVisit.setTypeVisit(configVisitDTO.getTypeVisit());
        configVisit.setDureeMoyen(configVisitDTO.getDureeMoyen());
        configVisit.setMoyenUtilise(configVisitDTO.getMoyenUtilise());

        return configVisitRepository.save(configVisit);
    }

    @Override
    public ConfigVisit getOne() {
        List<ConfigVisit> configVisits = configVisitRepository.findAll();

        if (!configVisits.isEmpty()) {
            return configVisits.get(0);
        }

        return new ConfigVisit();
    }
}
