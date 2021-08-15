package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.MeansUsedForVisit;
import cm.skysoft.app.dto.MeansUsedForVisitDTO;
import cm.skysoft.app.repository.MeansUsedForVisitReposiroty;
import cm.skysoft.app.service.AfbExternalService;
import cm.skysoft.app.service.MeansUsedForVisitService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeansUsedForVisitServiceImpl implements MeansUsedForVisitService {

    private final MeansUsedForVisitReposiroty meansUsedForVisitReposiroty;

    private final AfbExternalService afbExternalService;

    public MeansUsedForVisitServiceImpl(MeansUsedForVisitReposiroty meansUsedForVisitReposiroty, AfbExternalService afbExternalService) {
        this.meansUsedForVisitReposiroty = meansUsedForVisitReposiroty;
        this.afbExternalService = afbExternalService;
    }

    @Override
    public void save() {
        List<MeansUsedForVisitDTO> meansUsedForVisitDTOList = afbExternalService.getListMeansUsedForVisit();

        for (MeansUsedForVisitDTO m: meansUsedForVisitDTOList) {
            Optional<MeansUsedForVisit> meansUsedForVisit = findMeansUsedForVisit(m.getId());

            if(meansUsedForVisit.isPresent()){

                MeansUsedForVisit forVisit = meansUsedForVisit.get();
                MeansUsedForVisit usedForVisit = meansUsedForVisit.get();

                if(forVisit.getId() != null) {

                    if (!m.getName().getFr().equals(usedForVisit.getNameFr())) {
                        forVisit.setNameFr(m.getName().getFr());
                    }
                    if (!m.getName().getEn().equals(usedForVisit.getNameEn())) {
                        forVisit.setNameEn(m.getName().getEn());
                    }
                    meansUsedForVisitReposiroty.save(forVisit);
                }

            } else {
                MeansUsedForVisit usedForVisit = new MeansUsedForVisit();

                usedForVisit.setIdMeansUsedAfb(m.getId());
                usedForVisit.setNameEn(m.getName().getEn());
                usedForVisit.setNameFr(m.getName().getFr());

                meansUsedForVisitReposiroty.save(usedForVisit);
            }
        }
    }

    @Override
    public Optional<MeansUsedForVisit> findMeansUsedForVisit(Long idMeansUsedAfb) {
        return meansUsedForVisitReposiroty.findMeansUsedForVisitsByIdMeansUsedAfb(idMeansUsedAfb);
    }

    @Override
    public List<MeansUsedForVisit> findAll() {
        return meansUsedForVisitReposiroty.findAll();
    }
}
