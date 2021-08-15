package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.ProductVisitNote;
import cm.skysoft.app.domain.Products;
import cm.skysoft.app.repository.ProductVisitNoteRepository;
import cm.skysoft.app.service.ProductVisitNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVisitNoteServiceImpl implements ProductVisitNoteService {

    @Autowired
    private ProductVisitNoteRepository productVisitNoteRepository;

    @Override
    public ProductVisitNote save(ProductVisitNote productVisitNote) {
        return productVisitNoteRepository.save(productVisitNote);
    }

    @Override
    public List<ProductVisitNote> loadAll() {
        return productVisitNoteRepository.findAll();
    }

    @Override
    public List<ProductVisitNote> loadAllByVisitNoteId(Long visitId) {
        return productVisitNoteRepository.findProductVisitNoteByVisitNoteId(visitId);
    }

    @Override
    public void deleteAllProducts(List<ProductVisitNote> products) {
        productVisitNoteRepository.deleteAll(products);
    }

    @Override
    public List<Products> findProductByVisitNote_Id(Long visitNoteId) {
        return productVisitNoteRepository.findProductByVisitNoteId(visitNoteId);
    }
}
