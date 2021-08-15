package cm.skysoft.app.repository;

import cm.skysoft.app.domain.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Logs entity.
 */
@Repository
public interface LogsRepository extends JpaRepository<Logs, Long>, JpaSpecificationExecutor<Logs> {

}
