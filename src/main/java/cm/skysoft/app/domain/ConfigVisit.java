package cm.skysoft.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by francis on 6/23/21.
 */

@Entity
@Table(name = "config_visit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfigVisit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_visit")
    private String typeVisit;

    @Column(name = "duree_moyen")
    private int dureeMoyen;

    @Column(name = "moyen_utilise")
    private String moyenUtilise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeVisit() {
        return typeVisit;
    }

    public void setTypeVisit(String typeVisit) {
        this.typeVisit = typeVisit;
    }

    public int getDureeMoyen() {
        return dureeMoyen;
    }

    public void setDureeMoyen(int dureeMoyen) {
        this.dureeMoyen = dureeMoyen;
    }

    public String getMoyenUtilise() {
        return moyenUtilise;
    }

    public void setMoyenUtilise(String moyenUtilise) {
        this.moyenUtilise = moyenUtilise;
    }

    @Override
    public String toString() {
        return "ConfigVisit{" +
                "id=" + id +
                ", typeVisit='" + typeVisit + '\'' +
                ", dureeMoyen=" + dureeMoyen +
                ", moyenUtilise='" + moyenUtilise + '\'' +
                '}';
    }
}
