package cm.skysoft.app.service.impl;

import cm.skysoft.app.config.ApplicationProperties;
import cm.skysoft.app.domain.ConfigApplication;
import cm.skysoft.app.dto.ConfigApplicationDTO;
import cm.skysoft.app.repository.ConfigApplicationRepository;
import cm.skysoft.app.service.ConfigApplicationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigApplicationServiceImpl implements ConfigApplicationService {

    private final ConfigApplicationRepository configApplicationRepository;
    private final ApplicationProperties applicationProperties;

    public ConfigApplicationServiceImpl(ConfigApplicationRepository configApplicationRepository, ApplicationProperties applicationProperties) {
        this.configApplicationRepository = configApplicationRepository;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public ConfigApplication update(ConfigApplicationDTO configApplicationDTO) {

        Optional<ConfigApplication> config = configApplicationRepository.findByCodeConfigApplication(configApplicationDTO.getCodeConfigApplication());

        if(config.isPresent()) {
            config.get().setPatternEmissionSuggestion(configApplicationDTO.getPatternEmissionSuggestion());
            config.get().setPatternExecutionVisit(configApplicationDTO.getPatternExecutionVisit());
            config.get().setPatternNotValidatedSuggestion(configApplicationDTO.getPatternNotValidatedSuggestion());
            config.get().setPatternValidatedSuggestion(configApplicationDTO.getPatternValidatedSuggestion());
            config.get().setPatternValidatedPlannedVisit(configApplicationDTO.getPatternValidatedPlannedVisit());
            config.get().setPatternUpdatePlannedVisit(configApplicationDTO.getPatternUpdatePlannedVisit());
            config.get().setPatternValidatedVisitWithParticipant(configApplicationDTO.getPatternValidatedVisitWithParticipant());
            config.get().setLinkBehaviorForage(configApplicationDTO.getLinkBehaviorForage());
        }

        return config.map(configApplicationRepository::save).orElse(null);
    }

    @Override
    public ConfigApplication save() {
        ConfigApplication configApplications = findOne();
        ConfigApplication configApplication = new ConfigApplication();

        if(configApplications == null ) {
            configApplication.setId(1L);
            configApplication.setCodeConfigApplication(applicationProperties.getConfigApplication().getCodeConfigApplication());
            configApplication.setPatternEmissionSuggestion(applicationProperties.getConfigApplication().getPatternEmmissionSuggestion());
            configApplication.setPatternExecutionVisit(applicationProperties.getConfigApplication().getPatternExecutionVisit());
            configApplication.setPatternNotValidatedSuggestion(applicationProperties.getConfigApplication().getPatternNotValidatedSuggestion());
            configApplication.setPatternValidatedSuggestion(applicationProperties.getConfigApplication().getPatternValidatedSuggestion());
            configApplication.setPatternValidatedPlannedVisit(applicationProperties.getConfigApplication().getPatternValidatedPlannedVisit());
            configApplication.setPatternUpdatePlannedVisit(applicationProperties.getConfigApplication().getPatternUpdatePlannedVisit());
            configApplication.setPatternValidatedVisitWithParticipant(applicationProperties.getConfigApplication().getPatternValidatedVisitWithParticipant());
            configApplication.setLinkBehaviorForage(applicationProperties.getConfigApplication().getLinkBehaviorForage());

            return configApplicationRepository.save(configApplication);
        }

        return null;
    }

    @Override
    public Optional<ConfigApplication> findByCode(String code) {
        return configApplicationRepository.findByCodeConfigApplication(code);
    }

    @Override
    public ConfigApplication findOne() {
        List<ConfigApplication> configApplications = configApplicationRepository.findAll();

        if (!configApplications.isEmpty()) {
            return configApplications.get(0);
        }

        return null;
    }

    @Override
    public ConfigApplication getOne() {
        List<ConfigApplication> configVisits = configApplicationRepository.findAll();

        if (!configVisits.isEmpty()) {
            return configVisits.get(0);
        }

        return new ConfigApplication();
    }
}
