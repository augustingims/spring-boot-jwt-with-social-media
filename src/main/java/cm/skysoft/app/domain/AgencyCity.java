package cm.skysoft.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by francis on 2/11/21.
 */
@Entity
@Table(name = "agency_city")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgencyCity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_agency_city")
    private Long idAgencyCity;

    @Column(name = "agency_city_name_fr")
    private String agencyCityNameFr;

    @Column(name = "agency_city_name_en")
    private String agencyCityNameEn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAgencyCity() {
        return idAgencyCity;
    }

    public void setIdAgencyCity(Long idAgencyCity) {
        this.idAgencyCity = idAgencyCity;
    }

    public String getAgencyCityNameFr() {
        return agencyCityNameFr;
    }

    public void setAgencyCityNameFr(String agencyCityNameFr) {
        this.agencyCityNameFr = agencyCityNameFr;
    }

    public String getAgencyCityNameEn() {
        return agencyCityNameEn;
    }

    public void setAgencyCityNameEn(String agencyCityNameEn) {
        this.agencyCityNameEn = agencyCityNameEn;
    }

    @Override
    public String toString() {
        return "AgencyCity{" +
                "id=" + id +
                ", idAgencyCity=" + idAgencyCity +
                ", agencyCityNameFr='" + agencyCityNameFr + '\'' +
                ", agencyCityNameEn='" + agencyCityNameEn + '\'' +
                '}';
    }
}
