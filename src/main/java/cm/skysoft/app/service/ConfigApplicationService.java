package cm.skysoft.app.service;

import cm.skysoft.app.domain.ConfigApplication;
import cm.skysoft.app.dto.ConfigApplicationDTO;

import java.util.Optional;

public interface ConfigApplicationService {
    ConfigApplication update(ConfigApplicationDTO configApplicationDTO);
    ConfigApplication save();
    Optional<ConfigApplication> findByCode(String code);
    ConfigApplication findOne();
    ConfigApplication getOne();
}
