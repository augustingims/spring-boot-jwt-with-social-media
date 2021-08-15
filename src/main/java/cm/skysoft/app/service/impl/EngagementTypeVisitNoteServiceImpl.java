package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.EngagementType;
import cm.skysoft.app.domain.EngagementTypeVisitNote;
import cm.skysoft.app.repository.EngagementTypeVisitNoteRepository;
import cm.skysoft.app.service.EngagementTypeVisitNoteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EngagementTypeVisitNoteServiceImpl implements EngagementTypeVisitNoteService {

    private final EngagementTypeVisitNoteRepository engagementTypeVisitNoteRepository;

    public EngagementTypeVisitNoteServiceImpl(EngagementTypeVisitNoteRepository engagementTypeVisitNoteRepository) {
        this.engagementTypeVisitNoteRepository = engagementTypeVisitNoteRepository;
    }

    @Override
    public EngagementTypeVisitNote save(EngagementTypeVisitNote engagementTypeVisitNote) {
        return engagementTypeVisitNoteRepository.save(engagementTypeVisitNote);
    }

    @Override
    public List<EngagementTypeVisitNote> findAllByVisitNoteId(Long visitNoteId) {
        return engagementTypeVisitNoteRepository.findEngagementTypeVisitNoteByVisitNoteId(visitNoteId);
    }

    @Override
    public List<EngagementType> findEngagementTypeByVisitNoteId(Long visitNoteId) {
        return  engagementTypeVisitNoteRepository.findEngagementTypeByVisitNoteId(visitNoteId);
    }

    @Override
    public Optional<EngagementTypeVisitNote> findByEngagementTypeId(Long engaegmentTypeId) {
        return engagementTypeVisitNoteRepository.findById(engaegmentTypeId);
    }

    @Override
    public void deleteAllEngagementType(List<EngagementTypeVisitNote> engagementTypeVisitNotes) {
        engagementTypeVisitNoteRepository.deleteAll(engagementTypeVisitNotes);
    }
}
