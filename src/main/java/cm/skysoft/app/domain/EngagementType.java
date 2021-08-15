package cm.skysoft.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "engagement_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EngagementType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_engagement_type_afb")
    private Long idEngagementTypeAfb;

    @Column(name = "name_fr")
    private String nameFr;

    @Column(name = "name_en")
    private String nameEn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEngagementTypeAfb() {
        return idEngagementTypeAfb;
    }

    public void setIdEngagementTypeAfb(Long idEngagementTypeAfb) {
        this.idEngagementTypeAfb = idEngagementTypeAfb;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public String toString() {
        return "EngagementType{" +
                "id=" + id +
                ", idEngagementTypeAfb=" + idEngagementTypeAfb +
                ", nameFr='" + nameFr + '\'' +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }
}
