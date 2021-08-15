package cm.skysoft.app.domain;

import cm.skysoft.app.utils.MethoUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by francis on 2/11/21.
 */
@Entity
@Table(name = "visits")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Visits implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String termine  = "TERMINÉ";
    private static final String en_cour  = "EN COURS";
    private static final String en_attente  = "EN ATTENTE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "visit_code")
    private String visitCode;

    @Column(name = "localisation")
    private String localisation;

    @Lob
    @Column(name = "visit_object")
    private String visitObject;

    @Column(name = "visit_date")
    private LocalDateTime visitDate;

    @Column(name = "hour_visit")
    private String hourVisit;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_validated")
    private LocalDateTime dateValidated;

    @Column(name = "date_last_update")
    private LocalDateTime dateLastUpdate;

    @Column(name = "planification")
    private boolean planification = Boolean.FALSE;

    @Column(name = "preparation")
    private boolean preparation = Boolean.FALSE;

    @Column(name = "execution")
    private boolean execution = Boolean.FALSE;

    @Column(name = "reporting")
    private boolean reporting = Boolean.FALSE;

    @Column(name = "generate_report")
    private boolean generateReport = Boolean.FALSE;

    @Column(name = "save_execution", nullable = true)
    private boolean saveExecution = Boolean.FALSE;

    @Column(name = "archivate")
    private boolean archivate = Boolean.FALSE;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_afb", referencedColumnName = "id", nullable = false)
    private User userAfb;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client", referencedColumnName = "id", nullable = false)
    private Clients client;

    @Column(name = "moyen_utilise")
    private String moyenUtilise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getVisitCode() {
        return visitCode;
    }

    public void setVisitCode(String visitCode) {
        this.visitCode = visitCode;
    }

    public String getVisitObject() {
        return visitObject;
    }

    public void setVisitObject(String visitObject) {
        this.visitObject = visitObject;
    }

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public boolean isPlanification() {
        return planification;
    }

    public void setPlanification(boolean planification) {
        this.planification = planification;
    }

    public boolean isPreparation() {
        return preparation;
    }

    public void setPreparation(boolean preparation) {
        this.preparation = preparation;
    }

    public boolean isExecution() {
        return execution;
    }

    public void setExecution(boolean execution) {
        this.execution = execution;
    }

    public boolean isReporting() {
        return reporting;
    }

    public void setReporting(boolean reporting) {
        this.reporting = reporting;
    }

    public boolean isArchivate() {
        return archivate;
    }

    public void setArchivate(boolean archivate) {
        this.archivate = archivate;
    }

    public User getUserAfb() {
        return userAfb;
    }

    public void setUserAfb(User userAfb) {
        this.userAfb = userAfb;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public String getMoyenUtilise() {
        return moyenUtilise;
    }

    public void setMoyenUtilise(String moyenUtilise) {
        this.moyenUtilise = moyenUtilise;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getHourVisit() {
        return hourVisit;
    }

    public void setHourVisit(String hourVisit) {
        this.hourVisit = hourVisit;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getDateCreatedText() {
        return MethoUtils.getLocalDateTimeToString(dateCreated);
    }

    public String getVisitDateText() {

        return MethoUtils.getDateToString(visitDate);
    }

    public String getVisitDateUpdateText() {

        return MethoUtils.getDatePipeToString(visitDate);
    }

    public LocalDateTime getDateValidated() {
        return dateValidated;
    }

    public void setDateValidated(LocalDateTime dateValidated) {
        this.dateValidated = dateValidated;
    }

    public String getDateValidatedText() {
        return MethoUtils.getLocalDateTimeToString(dateValidated);
    }

    public String getVisitDateText2() {
        return MethoUtils.getLocalDateTime2ToString(visitDate);
    }

    public String getVisitDateTimeText() {
        return MethoUtils.getDatesTimeToString(visitDate);
    }

    public String getVisitDateTimeText2() {
        return MethoUtils.getDatesTimeToString(visitDate);
    }

    public LocalDateTime getDateLastUpdate() {
        return dateLastUpdate;
    }

    public String getDateLastUpdateText() {
        return MethoUtils.getLocalDateTimeToString(dateLastUpdate);
    }

    public void setDateLastUpdate(LocalDateTime dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }

    public String getStatusPlanificationText(){

        String s = null;

        if (planification) {
            s = termine;
        }

        return s;
    }

    public boolean getStatusPlanificationBoolean(){

        boolean s = false;

        if (planification) {
            s = Boolean.TRUE;
        }

        return s;
    }

    public String getStatusPreparationText(){

        String s;

        if (planification && preparation) {
            s = termine;
        } else {
            s = en_cour;
        }

        return s;
    }

    public boolean getStatusPreparationBoolean(){

        boolean s;

        if (planification && preparation) {
            s = Boolean.TRUE;
        } else {
            s = Boolean.FALSE;
        }

        return s;
    }

    public String getStatusExecutionText(){

        String s = null;

        if (preparation && execution) {
            s = termine;
        } else if(preparation) {
            s = en_cour;
        } else if (!execution) {
            s = en_attente;
        }

        return s;
    }

    public boolean getStatusExecutionBoolean(){

        boolean s = false;

        if (preparation && execution) {
            s =  Boolean.TRUE;
        }

        return s;
    }

    public String getStatusReportingText(){

        String s = null;

        if (execution && reporting) {
            s = termine;
        } else if(execution) {
            s = en_cour;
        } else if (!reporting) {
            s = en_attente;
        }

        return s;
    }

    public boolean getStatusReportingBoolean(){

        boolean s = false;

        if (execution && reporting) {
            s = Boolean.TRUE;
        } else if(execution) {
            s = Boolean.TRUE;
        }
        return s;
    }

    public String getStatusArchivageText(){

        String s = null;

        if (reporting && archivate) {
            s = termine;
        } else if(reporting) {
            s = en_cour;
        } else {
            s = en_attente;
        }

        return s;
    }

    public boolean getStatusArchivageBoolean(){

        boolean s = false;

        if (reporting && archivate) {
            s = Boolean.TRUE;
        } else if(reporting) {
            s = Boolean.TRUE;
        }

        return s;
    }

    public boolean isGenerateReport() {
        return generateReport;
    }

    public void setGenerateReport(boolean generateReport) {
        this.generateReport = generateReport;
    }

    public boolean isSaveExecution() {
        return saveExecution;
    }

    public void setSaveExecution(boolean saveExecution) {
        this.saveExecution = saveExecution;
    }

    @PostPersist
    @PostUpdate
    void p() {
        visitCode = visitCode == null ? MethoUtils.format(id.intValue())
                : visitCode;
    }

    @Override
    public String toString() {
        return "Visits{" +
                "id=" + id +
                ", visitType='" + visitType + '\'' +
                ", visitObject='" + visitObject + '\'' +
                ", visitDate=" + visitDate +
                ", planification=" + planification +
                ", preparation=" + preparation +
                ", execution=" + execution +
                ", reporting=" + reporting +
                ", archivate=" + archivate +
                ", userAfb=" + userAfb +
                ", client=" + client +
                '}';
    }
}
