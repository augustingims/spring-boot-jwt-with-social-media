package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.EngagementType;
import cm.skysoft.app.dto.EngagementTypeDTO;
import cm.skysoft.app.repository.EngagementTypeRepository;
import cm.skysoft.app.service.AfbExternalService;
import cm.skysoft.app.service.EngagementTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EngagementTypeServiceImpl implements EngagementTypeService {

    private final EngagementTypeRepository engagementTypeRepository;
    private final AfbExternalService afbExternalService;

    public EngagementTypeServiceImpl(EngagementTypeRepository engagementTypeRepository, AfbExternalService afbExternalService) {
        this.engagementTypeRepository = engagementTypeRepository;
        this.afbExternalService = afbExternalService;
    }

    @Override
    public void save() {
        List<EngagementTypeDTO> engagementTypeDTOList = afbExternalService.getEngagementType();

        for(EngagementTypeDTO engagementTypeDTO : engagementTypeDTOList) {
            Optional<EngagementType> engagementType = findEngagementTypeByIdEngagementAfb((long) engagementTypeDTO.getId());

            if(engagementType.isPresent()) {

                if (engagementType.get().getId() != null) {
                    if (!engagementTypeDTO.getName().getFr().equals(engagementType.get().getNameFr())) {
                        engagementType.get().setNameFr(engagementTypeDTO.getName().getFr());
                    }
                    if (!engagementTypeDTO.getName().getEn().equals(engagementType.get().getNameEn())) {
                        engagementType.get().setNameEn(engagementTypeDTO.getName().getEn());
                    }
                    engagementTypeRepository.save(engagementType.get());
                }
            } else {
                EngagementType type = new EngagementType();
                type.setIdEngagementTypeAfb((long) engagementTypeDTO.getId());
                type.setNameEn(engagementTypeDTO.getName().getEn());
                type.setNameFr(engagementTypeDTO.getName().getFr());

                engagementTypeRepository.save(type);
            }
        }
    }

    @Override
    public Optional<EngagementType> findEngagementTypeByIdEngagementAfb(Long idEngagementTypeAfb) {
        return engagementTypeRepository.findEngagementTypeByIdEngagementTypeAfb(idEngagementTypeAfb);
    }

    @Override
    public List<EngagementType> findAll() {
        return engagementTypeRepository.findAll();
    }
}
