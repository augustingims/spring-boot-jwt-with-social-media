package cm.skysoft.app.domain;

import javax.persistence.*;

@Entity
@Table(name = "means_used_by_visit")
public class MeansUsedForVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_means_used_afb")
    private Long idMeansUsedAfb;

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

    public Long getIdMeansUsedAfb() {
        return idMeansUsedAfb;
    }

    public void setIdMeansUsedAfb(Long idMeansUsedAfb) {
        this.idMeansUsedAfb = idMeansUsedAfb;
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
}
