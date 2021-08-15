package cm.skysoft.app.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Products implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_product_afb")
    private Long idProductAfb;

    @Column(name = "name_fr")
    private String nameFr;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "subscribed")
    private Boolean subscribed;

    @Column(name = "subscriptionDate")
    private String subscriptionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProductAfb() {
        return idProductAfb;
    }

    public void setIdProductAfb(Long idProductAfb) {
        this.idProductAfb = idProductAfb;
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

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    @Override
    public String toString() {
        return "Products{" +
                "id=" + id +
                ", idProductAfb=" + idProductAfb +
                ", nameFr='" + nameFr + '\'' +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }
}
