package cm.skysoft.app.repository;

import cm.skysoft.app.domain.User;
import cm.skysoft.app.domain.Visits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by francis on 2/12/21.
 */
public interface VisitsRepository extends JpaRepository<Visits, Long>, JpaSpecificationExecutor<Visits> {

    Visits findVisitsById(Long idVisit);

    @Query("select count(v) from Visits v where v.userAfb = :user and v.planification = FALSE")
    Long getNumberVisiteToPlanned(@Param("user") User user);

    @Query("select count(v) from Visits v, VisitParticipants vp where v.id = vp.visit.id and vp.user = :user and v.execution = FALSE")
    Long getNumberVisiteToPreparate(@Param("user") User user);

    @Query("select count(v) from Visits v where v.userAfb = :user and v.execution = FALSE and v.preparation = FALSE and v.planification = TRUE")
    Long getNumberVisiteCreatedToPreparate(@Param("user") User user);

    @Query("select count(v) from Visits v where v.userAfb = :user and v.execution = FALSE and v.preparation = TRUE")
    Long getNumberVisiteForExecuted(@Param("user") User user);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.execution = TRUE and v.preparation = TRUE and v.planification = TRUE  AND v.reporting = FALSE")
    Long getNumberVisiteToExcecuted(@Param("user") User user);

    Visits findVisitsByVisitCode(String codeVisit);

    @Query("select v from Visits v where v.execution = false ")
    List<Visits> findByExecution();

    /** Debut des requête SQL pour le dashborard */

    /**Begin get NumberTotalPlannedVisit */
    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.preparation = FALSE and v.planification = TRUE and v.archivate = FALSE and v.execution = FALSE and v.reporting = FALSE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalPlannedVisitOutsideTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.preparation = FALSE and v.planification = TRUE and v.archivate = FALSE and v.execution = FALSE and v.reporting = FALSE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalPlannedVisitAtTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.preparation = FALSE and v.planification = TRUE and v.archivate = FALSE and v.execution = FALSE and v.reporting = FALSE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalPlannedVisitPhoneCall(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalPlannedVisit */

    /**Begin get NumberTotalVisitNotExecuted */
    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.execution = FALSE and v.preparation = TRUE and v.planification = TRUE and v.archivate = FALSE and v.reporting = FALSE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalVisitNotExecutedOutsideTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.execution = FALSE and v.preparation = TRUE and v.planification = TRUE and v.archivate = FALSE and v.reporting = FALSE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalVisitNotExecutedAtTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.execution = FALSE and v.preparation = TRUE and v.planification = TRUE and v.archivate = FALSE and v.reporting = FALSE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalVisitNotExecutedPhoneCall(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalVisitNotExecuted */

    /**Begin get NumberTotalVisitAwaitingReport */
    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.reporting = FALSE  and v.preparation = TRUE and v.planification = TRUE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalVisitAwaitingReportOutsideTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.reporting = FALSE and v.preparation = TRUE and v.planification = TRUE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalVisitAwaitingReportAtTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.reporting = FALSE and v.preparation = TRUE and v.planification = TRUE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalVisitAwaitingReportPhoneCall(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalVisitAwaitingReport */

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.preparation = TRUE and v.planification = TRUE and v.execution = TRUE and v.reporting = TRUE ")
    Long getNumberTotalVisit(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalVisitAwaitingReport */

    //Pareil
    /**Begin get NumberTotalArchivatedVisit */
    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.preparation = TRUE and v.planification = TRUE and v.execution = TRUE and v.reporting = TRUE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalArchivateVisitOutsideTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.preparation = TRUE and v.planification = TRUE and v.execution = TRUE and v.reporting = TRUE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalArchivateVisitAtTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.preparation = TRUE and v.planification = TRUE and v.execution = TRUE and v.reporting = TRUE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalArchivateVisitPhoneCall(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalArchivatedVisit */
    //Pareil

    //Pareil
    /**Begin get NumberTotalVisitExecuted */
    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.preparation = TRUE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalVisitExecutedOutsideTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.preparation = TRUE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalVisitExecutedAtTheOffiche(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.userAfb = :user and  v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.preparation = TRUE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalVisitExecutedPhoneCall(@Param("user") User user, @Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalVisitExecuted */

    /** Fin des requête SQL pour le dashborard */






    /** Dashboard begin ADMIN */
    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.preparation = FALSE and v.planification = TRUE and v.archivate = FALSE and v.execution = FALSE and v.reporting = FALSE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalPlannedVisitOutsideTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.preparation = FALSE and v.planification = TRUE and v.archivate = FALSE and v.execution = FALSE and v.reporting = FALSE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalPlannedVisitAtTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.preparation = FALSE and v.planification = TRUE and v.archivate = FALSE and v.execution = FALSE and v.reporting = FALSE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalPlannedVisitPhoneCallAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalPlannedVisit */

    /**Begin get NumberTotalPreparedVisit */
    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.preparation = TRUE and v.planification = TRUE and v.execution = TRUE and v.reporting = TRUE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalArchivatedVisitOutsideTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.preparation = TRUE and v.planification = TRUE and v.execution = TRUE and v.reporting = TRUE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalArchivatedVisitAtTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.preparation = TRUE and v.planification = TRUE and v.execution = TRUE and v.reporting = TRUE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalArchivatedVisitPhoneCallAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalPreparedVisit */

    /**Begin get NumberTotalVisitExecuted */
    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.preparation = TRUE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalVisitExecutedOutsideTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.preparation = TRUE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalVisitExecutedAtTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.preparation = TRUE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalVisitExecutedPhoneCallAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalVisitExecuted */

    /**Begin get NumberTotalVisitNotExecuted */
    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.execution = FALSE and v.preparation = TRUE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalVisitNotExecutedOutsideTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.execution = FALSE and v.preparation = TRUE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalVisitNotExecutedAtTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.execution = FALSE and v.preparation = TRUE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalVisitNotExecutedPhoneCallAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalVisitNotExecuted */

    /**Begin get NumberTotalVisitAwaitingReport */
    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.reporting = FALSE and v.moyenUtilise = 'Déscente sur le terrain'")
    Long getNumberTotalVisitAwaitingReportOutsideTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.reporting = FALSE and v.moyenUtilise = 'Réception à la banque'")
    Long getNumberTotalVisitAwaitingReportAtTheOfficheAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.execution = TRUE and v.reporting = FALSE and v.moyenUtilise = 'Appel téléphonique'")
    Long getNumberTotalVisitAwaitingReportPhoneCallAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);
    /**End get NumberTotalVisitAwaitingReport */

    @Query("select count(v) from Visits v where v.dateCreated between :dateBefore and :dateAfter and v.preparation = TRUE and v.planification = TRUE and v.execution = TRUE and v.reporting = TRUE ")
    Long getNumberTotalVisitAdmin(@Param("dateBefore") LocalDateTime dateBefore, @Param("dateAfter") LocalDateTime dateAfter);

    /** Dashboard End ADMIN */
}
