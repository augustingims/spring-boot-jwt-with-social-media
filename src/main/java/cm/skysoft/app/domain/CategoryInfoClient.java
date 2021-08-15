package cm.skysoft.app.domain;

import javax.persistence.*;

/**
 * Created by Daniel 03/03/2021
 */

@Entity
@Table(name = "category_info_client")
public class CategoryInfoClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_fr")
    private String nameFr;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "id_category_afb")
    private Long idCategoryAfb;

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

    public Long getIdCategoryAfb() {
        return idCategoryAfb;
    }

    public void setIdCategoryAfb(Long idCategoryAfb) {
        this.idCategoryAfb = idCategoryAfb;
    }
}
