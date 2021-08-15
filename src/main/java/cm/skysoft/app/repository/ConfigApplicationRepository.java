package cm.skysoft.app.repository;

import cm.skysoft.app.domain.ConfigApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigApplicationRepository extends JpaRepository<ConfigApplication, Long> {

    Optional<ConfigApplication> findByCodeConfigApplication(String code);
}
