package cm.skysoft.app.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "engagement_type_visit_note")
public class EngagementTypeVisitNote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private VisitNote visitNote;

    @ManyToOne(optional = false)
    private EngagementType engagementType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VisitNote getVisitNote() {
        return visitNote;
    }

    public void setVisitNote(VisitNote visitNote) {
        this.visitNote = visitNote;
    }

    public EngagementType getEngagementType() {
        return engagementType;
    }

    public void setEngagementType(EngagementType engagementType) {
        this.engagementType = engagementType;
    }
}
