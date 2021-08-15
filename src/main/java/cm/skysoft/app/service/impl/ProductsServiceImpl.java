package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.Products;
import cm.skysoft.app.dto.ProductDTO;
import cm.skysoft.app.repository.ProductsRepository;
import cm.skysoft.app.service.AfbExternalService;
import cm.skysoft.app.service.ProductsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;

    private final AfbExternalService afbExternalService;

    public ProductsServiceImpl(ProductsRepository productsRepository, AfbExternalService afbExternalService) {
        this.productsRepository = productsRepository;
        this.afbExternalService = afbExternalService;
    }

    @Override
    public void save() {
        List<ProductDTO> productDTOList = afbExternalService.getListProducts();

        for (ProductDTO p: productDTOList) {
            Optional<Products> products = findProductsByIdProductAfb(p.getId());

            if(products.isPresent()){

                Products products1 = products.get();
                Products products2 = products.get();

                if(products1.getId() != null) {

                    if (!p.getName().getFr().equals(products2.getNameFr())) {
                        products1.setNameFr(p.getName().getFr());
                    }
                    if (!p.getName().getEn().equals(products2.getNameEn())) {
                        products1.setNameEn(p.getName().getEn());
                    }
                    if (!p.getSubscribed().equals(products2.getSubscribed())) {
                        products1.setSubscribed(p.getSubscribed());
                    }
//                    if (!p.getSubscriptionDate().equals(products2.getSubscriptionDate())) {
//                        products1.setSubscriptionDate(p.getSubscriptionDate());
//                    }
                    productsRepository.save(products1);
                }

            } else {
                Products products1 = new Products();

                products1.setIdProductAfb(p.getId());
                products1.setNameEn(p.getName().getEn());
                products1.setNameFr(p.getName().getFr());
                products1.setSubscribed(p.getSubscribed());
//                products1.setSubscriptionDate(p.getSubscriptionDate());

                productsRepository.save(products1);
            }
        }
    }

    @Override
    public Optional<Products> findProductsByIdProductAfb(Long idProductAfb) {
        return productsRepository.findProductsByIdProductAfb(idProductAfb);
    }

    @Override
    public Optional<Products> findProductsByIdProduct(Long idProduct) {
        return productsRepository.findById(idProduct);
    }

    @Override
    public List<Products> findAll() {
        return productsRepository.findAll();
    }
}
