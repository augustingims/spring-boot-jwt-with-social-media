package cm.skysoft.app.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "suggestion_participants")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SuggestionParticipants implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "suggestions", referencedColumnName = "id", nullable = false)
    private Suggestions suggestions;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Suggestions getSuggestion() {
        return suggestions;
    }

    public void setSuggestion(Suggestions suggestions) {
        this.suggestions = suggestions;
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
                ", suggestions=" + suggestions +
                ", user=" + user +
                '}';
    }
}
