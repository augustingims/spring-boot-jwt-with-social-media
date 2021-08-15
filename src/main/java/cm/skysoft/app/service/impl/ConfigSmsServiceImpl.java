package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.ConfigSms;
import cm.skysoft.app.dto.ConfigSmsDTO;
import cm.skysoft.app.repository.ConfigSmsRepository;
import cm.skysoft.app.service.ConfigSmsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by francis on 7/2/21.
 */
@Service
public class ConfigSmsServiceImpl implements ConfigSmsService {

    private final ConfigSmsRepository configSmsRepository;

    public ConfigSmsServiceImpl(ConfigSmsRepository configSmsRepository) {
        this.configSmsRepository = configSmsRepository;
    }

    @Override
    public ConfigSms save(ConfigSmsDTO configSmsDTO) {
        ConfigSms configSms = new ConfigSms();
        configSms.setId(configSmsDTO.getId());
        configSms.setUserSms(configSmsDTO.getUserSms());
        configSms.setPasswordSms(configSmsDTO.getPasswordSms());
        configSms.setSenderSms(configSmsDTO.getSenderSms());
        configSms.setNombreChiffreTelephone(configSmsDTO.getNombreChiffreTelephone());
        configSms.setSendSmsNotification(configSmsDTO.getSendSmsNotification());
        configSms.setSendSmsEmissionSugestion(configSmsDTO.getSendSmsEmissionSugestion());
        configSms.setSendSmsValidationSugestion(configSmsDTO.getSendSmsValidationSugestion());
        configSms.setSendSmsNonValidationSugestion(configSmsDTO.getSendSmsNonValidationSugestion());
        configSms.setSendSmsExecutionVisite(configSmsDTO.getSendSmsExecutionVisite());
        configSms.setSendSmsModificationVisite(configSmsDTO.getSendSmsModificationVisite());
        return configSmsRepository.save(configSms);
    }

    @Override
    public ConfigSms getOne() {
        List<ConfigSms> configSms = configSmsRepository.findAll();

        if (!configSms.isEmpty()) {
            return configSms.get(0);
        }

        return null;
    }
}
