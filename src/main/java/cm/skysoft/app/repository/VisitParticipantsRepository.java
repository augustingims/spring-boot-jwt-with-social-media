package cm.skysoft.app.repository;

import cm.skysoft.app.domain.User;
import cm.skysoft.app.domain.VisitParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by francis on 3/2/21.
 */
public interface VisitParticipantsRepository extends JpaRepository<VisitParticipants, Long> {

    @Query("SELECT vp.user From VisitParticipants vp WHERE vp.visit.id =:idVisit")
    List<User> getAllUserParticipants(@Param(value = "idVisit")Long idVisit);

    @Query("SELECT vp From VisitParticipants vp WHERE vp.visit.id =:idVisit")
    List<VisitParticipants> getVisitParticipantsByIdVisit(@Param(value = "idVisit")Long idVisit);
}
