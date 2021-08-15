package cm.skysoft.app.repository;

import cm.skysoft.app.domain.UserAfbs;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by francis on 2/13/21.
 */
public interface UserAfbsRepository extends JpaRepository<UserAfbs, Long> {
}
