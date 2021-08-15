package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.VisitType;
import cm.skysoft.app.dto.VisitTypeDTO;
import cm.skysoft.app.repository.VisitTypeRepository;
import cm.skysoft.app.service.AfbExternalService;
import cm.skysoft.app.service.VisitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitTypeServiceImpl implements VisitTypeService {

    @Autowired
    private VisitTypeRepository visitTypeRepository;

    @Autowired
    private AfbExternalService afbExternalService;

    @Override
    public void save() {

        List<VisitTypeDTO> visitTypeDTOList = afbExternalService.getListVisitTypeDtos();

        for (VisitTypeDTO v: visitTypeDTOList) {

            Optional<VisitType> visitType = findOne(v.getId());

            if (visitType.isPresent()){

                VisitType visitType1 = visitType.get();

                if(visitType1.getId() != null) {

                    if (!v.getName().getFr().equals(visitType1.getNameFr())) {
                        visitType1.setNameFr(v.getName().getFr());
                    }
                    if (!v.getName().getEn().equals(visitType1.getNameEn())) {
                        visitType1.setNameEn(v.getName().getEn());
                    }
                    visitTypeRepository.save(visitType1);
                }
            } else {

                VisitType visitType1 = new VisitType();

                visitType1.setIdVisitType(v.getId());
                visitType1.setNameEn(v.getName().getEn());
                visitType1.setNameFr(v.getName().getFr());

                visitTypeRepository.save(visitType1);
            }
        }
    }

    @Override
    public VisitType update(VisitTypeDTO visitTypeDTO) {
        return null;
    }

    @Override
    public List<VisitType> findAll() {
        return visitTypeRepository.findAll();
    }

    @Override
    public Optional<VisitType> findOne(Long id) {
        return visitTypeRepository.findVisitTypeByIdVisitType(id);
    }
}
