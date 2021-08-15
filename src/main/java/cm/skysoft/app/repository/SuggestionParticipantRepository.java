package cm.skysoft.app.repository;

import cm.skysoft.app.domain.SuggestionParticipants;
import cm.skysoft.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SuggestionParticipantRepository extends JpaRepository<SuggestionParticipants, Long> {

    @Query("SELECT vp.user From SuggestionParticipants vp WHERE vp.suggestions.id =:idSuggestion")
    List<User> getAllUserParticipants(@Param(value = "idSuggestion")Long idSuggestion);
}
