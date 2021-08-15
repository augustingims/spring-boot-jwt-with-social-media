package cm.skysoft.app.service;

import cm.skysoft.app.domain.CategoryInfoClient;

import java.util.List;
import java.util.Optional;

public interface CategoryInfoClientService {

    /**
     * Save category info client
     */
    void save();

    /**
     * find one category information client
     * @param idCategoryInfoClientAfb
     */
    Optional<CategoryInfoClient> findOne(Long idCategoryInfoClientAfb);

    /**
     * get All categoty info client
     * @return all category info client
     */
    List<CategoryInfoClient> findAll();
}
