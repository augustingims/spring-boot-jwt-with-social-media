package cm.skysoft.app.service;

import cm.skysoft.app.domain.Products;

import java.util.List;
import java.util.Optional;

public interface ProductsService {

    /**
     * save products in db
     */
    void save();

    /**
     * get product afb
     * @param idProductAfb the product afb
     * @return product afb
     */
    Optional<Products> findProductsByIdProductAfb(Long idProductAfb);

    /**
     * get product
     * @param idProduct the product
     * @return product
     */
    Optional<Products> findProductsByIdProduct(Long idProduct);

    /**
     * get list products
     * @return list products
     */
    List<Products> findAll();

}
