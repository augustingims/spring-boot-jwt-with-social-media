package cm.skysoft.app.web;

import cm.skysoft.app.domain.CategoryInfoClient;
import cm.skysoft.app.service.CategoryInfoClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryInfoClientResource {

    private final CategoryInfoClientService categoryInfoClientService;

    public CategoryInfoClientResource(CategoryInfoClientService categoryInfoClientService) {
        this.categoryInfoClientService = categoryInfoClientService;
    }

    /**
     * get category info client
     * @return category info client
     */
    @GetMapping("/categoriesInfoClient")
    public List<CategoryInfoClient> findAll(){
        return categoryInfoClientService.findAll();
    }
}
