package cm.skysoft.app.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by francis on 2/11/21.
 */
@Entity
@Table(name = "agency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Agency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_agency")
    private Long idAgency;

    @Column(name = "agency_code")
    private String agencyCode;

    @Column(name = "name")
    private String name;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Name agencyName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agency_city", referencedColumnName = "id", nullable = false)
    private AgencyCity agencyCity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agency_region", referencedColumnName = "id", nullable = false)
    private AgencyRegion agencyRegion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agency_country", referencedColumnName = "id", nullable = false)
    private AgencyCountry agencyCountry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAgency() {
        return idAgency;
    }

    public void setIdAgency(Long idAgency) {
        this.idAgency = idAgency;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Name getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(Name agencyName) {
        this.agencyName = agencyName;
    }

    public AgencyCity getAgencyCity() {
        return agencyCity;
    }

    public void setAgencyCity(AgencyCity agencyCity) {
        this.agencyCity = agencyCity;
    }

    public AgencyRegion getAgencyRegion() {
        return agencyRegion;
    }

    public void setAgencyRegion(AgencyRegion agencyRegion) {
        this.agencyRegion = agencyRegion;
    }

    public AgencyCountry getAgencyCountry() {
        return agencyCountry;
    }

    public void setAgencyCountry(AgencyCountry agencyCountry) {
        this.agencyCountry = agencyCountry;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "id=" + id +
                ", idAgency=" + idAgency +
                ", agencyCode='" + agencyCode + '\'' +
                ", name='" + name + '\'' +
                ", agencyName=" + agencyName +
                ", agencyCity=" + agencyCity +
                ", agencyRegion=" + agencyRegion +
                ", agencyCountry=" + agencyCountry +
                '}';
    }
}
