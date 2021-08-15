package cm.skysoft.app.service;

import cm.skysoft.app.domain.ConfigSms;
import cm.skysoft.app.dto.ConfigSmsDTO;

/**
 * Created by francis on 6/23/21.
 */
public interface ConfigSmsService {

    ConfigSms save(ConfigSmsDTO configSmsDTO);

    ConfigSms getOne();
}
