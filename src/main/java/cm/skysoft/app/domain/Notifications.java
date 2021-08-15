package cm.skysoft.app.domain;

import cm.skysoft.app.utils.MethoUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by francis on 2/25/21.
 */
@Entity
@Table(name = "notifications")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_notification")
    private String typeNotification;

    @Lob
    @Column(name = "description_notification", columnDefinition = "LONGTEXT")
    private String descriptionNotification;

    @Column(name = "created_date")
    private LocalDateTime createDate;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "create_by", referencedColumnName = "id", nullable = false)
    private User createBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "send_to", referencedColumnName = "id", nullable = false)
    private User sendTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeNotification() {
        return typeNotification;
    }

    public void setTypeNotification(String typeNotification) {
        this.typeNotification = typeNotification;
    }

    public String getDescriptionNotification() {
        return descriptionNotification;
    }

    public void setDescriptionNotification(String descriptionNotification) {
        this.descriptionNotification = descriptionNotification;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getDateCreateText(){
        return MethoUtils.getLocalDateTimeToString(createDate);
    }

    public String getStatusText(){
        String statusText = "";

        if(status){
            return statusText = "LUE";
        } else {
            return statusText = "NON LUE";
        }
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getSendTo() {
        return sendTo;
    }

    public void setSendTo(User sendTo) {
        this.sendTo = sendTo;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "id=" + id +
                ", typeNotification='" + typeNotification + '\'' +
                ", descriptionNotification='" + descriptionNotification + '\'' +
                ", createDate=" + createDate +
                ", status=" + status +
                ", createBy=" + createBy +
                ", sendTo=" + sendTo +
                '}';
    }
}
