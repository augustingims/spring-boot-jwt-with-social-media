package cm.skysoft.app.repository;

import cm.skysoft.app.domain.CategoryInfoClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryInfoClientRepository extends JpaRepository<CategoryInfoClient, Long> {

    Optional<CategoryInfoClient> findCategoryInfoClientByIdCategoryAfb(Long idCategoryAfb);
}
