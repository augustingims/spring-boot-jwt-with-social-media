package cm.skysoft.app.domain;

import cm.skysoft.app.utils.MethoUtils;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "visit_note")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VisitNote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visit_code")
    private String visitCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visits", referencedColumnName = "id", nullable = false)
    private Visits visits;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
    private User user;

    @Lob
    @Column(name = "note_visit")
    private String noteVisit;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date__last_updated")
    private LocalDateTime dateLastUpdated;

    @Column(name = "date_validated")
    private LocalDateTime dateValidated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Visits getVisits() {
        return visits;
    }

    public void setVisits(Visits visits) {
        this.visits = visits;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNoteVisit() {
        return noteVisit;
    }

    public void setNoteVisit(String noteVisit) {
        this.noteVisit = noteVisit;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCreatedText(){
        return MethoUtils.getLocalDateTimeToString(dateCreated);
    }

    public LocalDateTime getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(LocalDateTime dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public String getVisitCode() {
        return visitCode;
    }

    public void setVisitCode(String visitCode) {
        this.visitCode = visitCode;
    }

    public LocalDateTime getDateValidated() {
        return dateValidated;
    }

    public String getDateValidatedText(){
        return MethoUtils.getLocalDateTimeToString(dateValidated);
    }

    public void setDateValidated(LocalDateTime dateValidated) {
        this.dateValidated = dateValidated;
    }

    @PostPersist
    @PostUpdate
    void p() {
        visitCode = visitCode == null ? MethoUtils.format(id.intValue())
                : visitCode;
    }

    @Override
    public String toString() {
        return "VisitNote{" +
                "id=" + id +
                ", visits=" + visits +
                ", user=" + user +
                ", noteVisit='" + noteVisit + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
