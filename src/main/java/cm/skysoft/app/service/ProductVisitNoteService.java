package cm.skysoft.app.service;

import cm.skysoft.app.domain.ProductVisitNote;
import cm.skysoft.app.domain.Products;

import java.util.List;

public interface ProductVisitNoteService {

    /**
     * save product by visiNote
     * @param productVisitNote by productVisitNote
     * @return save product by visiNote
     */
    ProductVisitNote save(ProductVisitNote productVisitNote);

    /**
     * list all product
     * @return list all product
     */
    List<ProductVisitNote> loadAll();

    /**
     * list products by visitNote id
     * @param visitId by visitId
     * @return list products by visitNote id
     */
    List<ProductVisitNote> loadAllByVisitNoteId(Long visitId);

    /**
     * delete all products
     * @param products by products
     */
    void deleteAllProducts(List<ProductVisitNote> products);

    /**
     * get list products by visitNoteId
     * @param visitNoteId by visitNoteId
     * @return list products by visitNoteId
     */
    List<Products> findProductByVisitNote_Id(Long visitNoteId);
}
