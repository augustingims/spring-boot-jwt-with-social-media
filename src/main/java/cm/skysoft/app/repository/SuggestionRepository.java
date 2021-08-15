package cm.skysoft.app.repository;

import cm.skysoft.app.domain.Suggestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by daniel on 2/18/21.
 */
public interface SuggestionRepository extends JpaRepository<Suggestions, Long>, JpaSpecificationExecutor<Suggestions> {
     Optional <Suggestions> findSuggestionsById(Long idSuggestion);

     @Query("SELECT COUNT(s.id) FROM Suggestions s WHERE s.userAfb.id = :idRecipientToUser AND s.status = FALSE ")
     Long getNumberSuggestionUnreadByUser(@Param(value = "idRecipientToUser") Long idRecipientToUser);

     Suggestions findSuggestionsByCodeSuggestion(@Param("codeSuggestion") String codeSuggestion);
}
