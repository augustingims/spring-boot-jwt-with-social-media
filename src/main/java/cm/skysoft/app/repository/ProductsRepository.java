package cm.skysoft.app.repository;

import cm.skysoft.app.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    Optional<Products> findProductsByIdProductAfb(Long idProductAfb);
}
