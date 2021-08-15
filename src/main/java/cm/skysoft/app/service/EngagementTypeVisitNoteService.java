package cm.skysoft.app.service;

import cm.skysoft.app.domain.EngagementType;
import cm.skysoft.app.domain.EngagementTypeVisitNote;

import java.util.List;
import java.util.Optional;

public interface EngagementTypeVisitNoteService {

    /**
     * create a new EngagementTypeVisitNote
     * @param engagementTypeVisitNote by
     * @return new EngagementTypeVisitNote
     */
    EngagementTypeVisitNote save(EngagementTypeVisitNote engagementTypeVisitNote);

    List<EngagementTypeVisitNote> findAllByVisitNoteId(Long visitNoteId);

    List<EngagementType> findEngagementTypeByVisitNoteId(Long visitNoteId);

    Optional<EngagementTypeVisitNote> findByEngagementTypeId(Long engaegmentTypeId);

    void deleteAllEngagementType(List<EngagementTypeVisitNote> engagementTypeVisitNotes);
}
