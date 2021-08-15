package cm.skysoft.app.service;

import cm.skysoft.app.domain.ConfigVisit;
import cm.skysoft.app.dto.ConfigVisitDTO;

/**
 * Created by francis on 6/23/21.
 */
public interface ConfigVisitService {

    ConfigVisit save(ConfigVisitDTO configVisitDTO);

    ConfigVisit getOne();
}
