package cm.skysoft.app.repository;

import cm.skysoft.app.domain.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by francis on 2/12/21.
 */
public interface AgencyRepository  extends JpaRepository<Agency, Long> {
    Agency findByIdAgency(Long idAgency);

    List<Agency> findAllByOrderByName();
}
