package cm.skysoft.app.web;

import cm.skysoft.app.domain.Products;
import cm.skysoft.app.service.ProductVisitNoteService;
import cm.skysoft.app.service.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsResource {

    private final ProductsService productsService;
    private final ProductVisitNoteService productVisitNoteService;

    public ProductsResource(ProductsService productsService, ProductVisitNoteService productVisitNoteService) {
        this.productsService = productsService;
        this.productVisitNoteService = productVisitNoteService;
    }

    @GetMapping
    public ResponseEntity<List<Products>> findAll(){
        List<Products> productsList = productsService.findAll();
        return ResponseEntity.ok().body(productsList);
    }

    @GetMapping("/{visitNoteId}")
    public ResponseEntity<List<Products>> findByIdVisitNote(@PathVariable Long visitNoteId){
        List<Products> productsList = productVisitNoteService.findProductByVisitNote_Id(visitNoteId);
        return new ResponseEntity<>(productsList, HttpStatus.OK);
    }
}
