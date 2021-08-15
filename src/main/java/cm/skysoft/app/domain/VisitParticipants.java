package cm.skysoft.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by francis on 3/2/21.
 */
@Entity
@Table(name = "visit_participants")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VisitParticipants implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visit", referencedColumnName = "id", nullable = false)
    private Visits visit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Visits getVisit() {
        return visit;
    }

    public void setVisit(Visits visit) {
        this.visit = visit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "VisitParticipants{" +
                "id=" + id +
                ", visit=" + visit +
                ", user=" + user +
                '}';
    }
}
