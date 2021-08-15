package cm.skysoft.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by francis on 4/26/21.
 */
@Entity
@Table(name = "visit_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VisitReport implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "resolution")
    private String resolution;

    @Column(name = "subject_exchanged")
    private String subjectExchanged;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visit_note", referencedColumnName = "id", nullable = false)
    private VisitNote visitNote;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "execution_delay")
    private String executionDelay;

    @Lob
    @Column(name = "summary_of_exchange")
    private String summaryOfExchange;

    public VisitReport() {
    }

    public VisitReport(String summaryOfExchange, String resolution, String subjectExchanged, String executionDelay, LocalDateTime creationDate, VisitNote visitNote) {
        this.resolution = resolution;
        this.visitNote = visitNote;
        this.creationDate = creationDate;
        this.executionDelay = executionDelay;
        this.summaryOfExchange = summaryOfExchange;
        this.subjectExchanged = subjectExchanged;
    }

    public VisitReport(Long id, String summaryOfExchange, String resolution, String subjectExchanged, String executionDelay, LocalDateTime creationDate, VisitNote visitNote) {
        this.id = id;
        this.resolution = resolution;
        this.visitNote = visitNote;
        this.creationDate = creationDate;
        this.executionDelay = executionDelay;
        this.summaryOfExchange = summaryOfExchange;
        this.subjectExchanged = subjectExchanged;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getSummaryOfExchange() {
        return summaryOfExchange;
    }

    public void setSummaryOfExchange(String summaryOfExchange) {
        this.summaryOfExchange = summaryOfExchange;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getExecutionDelay() {
        return executionDelay;
    }

    public void setExecutionDelay(String executionDelay) {
        this.executionDelay = executionDelay;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public VisitNote getVisitNote() {
        return visitNote;
    }

    public void setVisitNote(VisitNote visitNote) {
        this.visitNote = visitNote;
    }

    public String getSubjectExchanged() {
        return subjectExchanged;
    }

    public void setSubjectExchanged(String subjectExchanged) {
        this.subjectExchanged = subjectExchanged;
    }

    @Override
    public String toString() {
        return "VisitReport{" +
                "id=" + id +
                ", resolution='" + resolution + '\'' +
                ", subjectExchanged='" + subjectExchanged + '\'' +
                ", visitNote=" + visitNote +
                ", creationDate=" + creationDate +
                ", executionDelay='" + executionDelay + '\'' +
                ", summaryOfExchange='" + summaryOfExchange + '\'' +
                '}';
    }
}
