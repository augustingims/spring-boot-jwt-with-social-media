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
@Table(name = "suggestions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Suggestions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "motivation", columnDefinition = "LONGTEXT")
    private String motivation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visit", referencedColumnName = "id", nullable = false)
    private Visits visit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_afb", referencedColumnName = "id", nullable = false)
    private User userAfb;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_afb_expediteur", referencedColumnName = "id", nullable = false)
    private User userAfbExpediteur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client", referencedColumnName = "id", nullable = false)
    private Clients clients;

    @Column(name = "status", nullable = false)
    private boolean status = Boolean.FALSE;

    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @Column(name = "date_visite")
    private LocalDateTime dateVisit;

    @Column(name = "hour_visit")
    private String hourVisit;

    @Column(name = "moyen_utilise")
    private String moyenUtilise;


    @Column(name = "objectif_visite", columnDefinition = "LONGTEXT")
    private String visitObject;

    @Column(name = "type_visit")
    private String typeVisit;

    @Column(name = "code_suggestion")
    private String codeSuggestion;

    @Column(name = "validated", nullable = false)
    private Boolean validated = Boolean.FALSE;

    @Column(name = "not_validated", nullable = false)
    private Boolean notValidated = Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Visits getVisit() {
        return visit;
    }

    public void setVisit(Visits visit) {
        this.visit = visit;
    }

    public User getUserAfb() {
        return userAfb;
    }

    public void setUserAfb(User userAfb) {
        this.userAfb = userAfb;
    }

    public Clients getClients() {
        return clients;
    }

    public void setClients(Clients clients) {
        this.clients = clients;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUserAfbExpediteur() {
        return userAfbExpediteur;
    }

    public void setUserAfbExpediteur(User userAfbExpediteur) {
        this.userAfbExpediteur = userAfbExpediteur;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateCreateText(){
        return MethoUtils.getLocalDateTimeToString(dateCreate);
    }

    public LocalDateTime getDateVisit() {
        return dateVisit;
    }

    public void setDateVisit(LocalDateTime dateVisit) {
        this.dateVisit = dateVisit;
    }

    public String getMoyenUtilise() {
        return moyenUtilise;
    }

    public void setMoyenUtilise(String moyenUtilise) {
        this.moyenUtilise = moyenUtilise;
    }

    public String getVisitObject() {
        return visitObject;
    }

    public void setVisitObject(String visitObject) {
        this.visitObject = visitObject;
    }

    public String getTypeVisit() {
        return typeVisit;
    }

    public void setTypeVisit(String typeVisit) {
        this.typeVisit = typeVisit;
    }

    public String getCodeSuggestion() {
        return codeSuggestion;
    }

    public void setCodeSuggestion(String codeSuggestion) {
        this.codeSuggestion = codeSuggestion;
    }

    public String getVisitDateText() {
        return MethoUtils.getDateToString(dateVisit);
    }
    public String getVisitDateText2() {
        return MethoUtils.getLocalDateTime2ToString(dateVisit);
    }

    public String getVisitDateTimeText() {
        return MethoUtils.getDateTimeTextToString(dateVisit);
    }

    public Boolean getValidated() {
        return validated;
    }

    public String getHourVisit() {
        return hourVisit;
    }

    public void setHourVisit(String hourVisit) {
        this.hourVisit = hourVisit;
    }

    public Boolean getNotValidated() {
        return notValidated;
    }

    public void setNotValidated(Boolean notValidated) {
        this.notValidated = notValidated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    @PostPersist
    @PostUpdate
    void p() {
        codeSuggestion = codeSuggestion == null ? MethoUtils.format(id.intValue())
                : codeSuggestion;
    }

    @Override
    public String toString() {
        return "Suggestions{" +
                "id=" + id +
                ", motivation='" + motivation + '\'' +
                ", visit=" + visit +
                ", userAfb=" + userAfb +
                ", userAfbExpediteur=" + userAfbExpediteur +
                ", clients=" + clients +
                ", status=" + status +
                ", dateCreate=" + dateCreate +
                '}';
    }
}
