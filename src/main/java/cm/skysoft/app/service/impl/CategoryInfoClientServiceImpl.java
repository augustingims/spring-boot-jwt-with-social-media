package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.CategoryInfoClient;
import cm.skysoft.app.dto.ClientInformationCategoryDTO;
import cm.skysoft.app.repository.CategoryInfoClientRepository;
import cm.skysoft.app.service.AfbExternalService;
import cm.skysoft.app.service.CategoryInfoClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryInfoClientServiceImpl implements CategoryInfoClientService {

    @Autowired
    private CategoryInfoClientRepository categoryInfoClientRepository;

    @Autowired
    private AfbExternalService afbExternalService;

    @Override
    public void save() {

        List<ClientInformationCategoryDTO> clientInformationCategoryDTOList = afbExternalService.getListClientInformationCategory();

        for (ClientInformationCategoryDTO cl: clientInformationCategoryDTOList) {
            Optional<CategoryInfoClient> categoryInfoClientList = findOne(cl.getId());

            if(categoryInfoClientList.isPresent()){

                CategoryInfoClient categoryInfoClient = categoryInfoClientList.get();

                if(categoryInfoClient.getId() != null) {

                    if (!cl.getName().getFr().equals(categoryInfoClient.getNameFr())) {
                        categoryInfoClient.setNameFr(cl.getName().getFr());
                    }
                    if (!cl.getName().getEn().equals(categoryInfoClient.getNameEn())) {
                        categoryInfoClient.setNameEn(cl.getName().getEn());
                    }
                    categoryInfoClientRepository.save(categoryInfoClient);
                }

            } else {
                CategoryInfoClient categoryInfoClient = new CategoryInfoClient();

                categoryInfoClient.setIdCategoryAfb(cl.getId());
                categoryInfoClient.setNameEn(cl.getName().getEn());
                categoryInfoClient.setNameFr(cl.getName().getFr());

                categoryInfoClientRepository.save(categoryInfoClient);
            }
        }

    }

    @Override
    public Optional<CategoryInfoClient> findOne(Long idCategoryInfoClientAfb) {
        return categoryInfoClientRepository.findCategoryInfoClientByIdCategoryAfb(idCategoryInfoClientAfb);
    }

    @Override
    public List<CategoryInfoClient> findAll() {
        return categoryInfoClientRepository.findAll();
    }
}
