package cm.skysoft.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by francis on 2/11/21.
 */
@Entity
@Table(name = "agency_region")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgencyRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_agency_region")
    private Long idAgencyRegion;

    @Column(name = "agency_region_name")
    private String agencyRegionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAgencyRegion() {
        return idAgencyRegion;
    }

    public void setIdAgencyRegion(Long idAgencyRegion) {
        this.idAgencyRegion = idAgencyRegion;
    }

    public String getAgencyRegionName() {
        return agencyRegionName;
    }

    public void setAgencyRegionName(String agencyRegionName) {
        this.agencyRegionName = agencyRegionName;
    }

    @Override
    public String toString() {
        return "AgencyRegion{" +
                "id=" + id +
                ", idAgencyRegion=" + idAgencyRegion +
                ", agencyRegionName='" + agencyRegionName + '\'' +
                '}';
    }
}
