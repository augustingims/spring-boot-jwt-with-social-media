package cm.skysoft.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by francis on 2/11/21.
 */

@Entity
@Table(name = "clients")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Clients implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_client")
    private Long idClient;

    @Column(name = "activity_profile")
    private String activityProfile;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "birth_place")
    private String birthPlace;

    @Column(name = "code_client")
    private String codeClient;

    @Column(name = "email")
    private String email;

    @Column(name = "entry_date")
    private String entryDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "legal_form")
    private String legalForm;

    @Column(name = "particular_profile")
    private String particularProfile;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manager", referencedColumnName = "id", nullable = false)
    private User manager;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agency", referencedColumnName = "id", nullable = false)
    private Agency agency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getActivityProfile() {
        return activityProfile;
    }

    public void setActivityProfile(String activityProfile) {
        this.activityProfile = activityProfile;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
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

    public String getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }

    public String getParticularProfile() {
        return particularProfile;
    }

    public void setParticularProfile(String particularProfile) {
        this.particularProfile = particularProfile;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    @Override
    public String toString() {
        return "Clients{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", activityProfile='" + activityProfile + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", codeClient='" + codeClient + '\'' +
                ", email='" + email + '\'' +
                ", entryDate='" + entryDate + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", legalForm='" + legalForm + '\'' +
                ", particularProfile='" + particularProfile + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", manager=" + manager +
                ", agency=" + agency +
                '}';
    }
}
