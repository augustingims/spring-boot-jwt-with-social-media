package cm.skysoft.app.domain;

import javax.persistence.*;

/**
 * Created by Daniel 02/03/2021
 * Table Visit type
 */

@Entity
@Table(name = "visit_type")
public class VisitType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_visit_type")
    private Long idVisitType;

    public Long getIdVisitType() {
        return idVisitType;
    }

    public void setIdVisitType(Long idVisitType) {
        this.idVisitType = idVisitType;
    }

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
