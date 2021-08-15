package cm.skysoft.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by francis on 2/11/21.
 */
@Entity
@Table(name = "agency_country")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgencyCountry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_agency_country")
    private Long idAgencyCountry;

    @Column(name = "agency_country_name")
    private String agencyCountryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAgencyCountry() {
        return idAgencyCountry;
    }

    public void setIdAgencyCountry(Long idAgencyCountry) {
        this.idAgencyCountry = idAgencyCountry;
    }

    public String getAgencyCountryName() {
        return agencyCountryName;
    }

    public void setAgencyCountryName(String agencyCountryName) {
        this.agencyCountryName = agencyCountryName;
    }

    @Override
    public String toString() {
        return "AgencyCountry{" +
                "id=" + id +
                ", idAgencyCountry=" + idAgencyCountry +
                ", agencyCountryName='" + agencyCountryName + '\'' +
                '}';
    }
}
