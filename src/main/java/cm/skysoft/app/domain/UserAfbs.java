package cm.skysoft.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by francis on 2/11/21.
 */
@Entity
@Table(name = "user_afbs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserAfbs implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_user_afb")
    private Long idUserAfb;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "sex")
    private String sex;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "icn")
    private String icn;

    @Column(name = "position_name")
    private String positionName;

    @Column(name = "user_code")
    private String userCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agency", referencedColumnName = "id", nullable = false)
    private Agency agency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUserAfb() {
        return idUserAfb;
    }

    public void setIdUserAfb(Long idUserAfb) {
        this.idUserAfb = idUserAfb;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIcn() {
        return icn;
    }

    public void setIcn(String icn) {
        this.icn = icn;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    @Override
    public String toString() {
        return "UserAfbs{" +
                "id=" + id +
                ", idUserAfb=" + idUserAfb +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", icn='" + icn + '\'' +
                ", positionName='" + positionName + '\'' +
                ", userCode='" + userCode + '\'' +
                ", agency=" + agency +
                '}';
    }
}
