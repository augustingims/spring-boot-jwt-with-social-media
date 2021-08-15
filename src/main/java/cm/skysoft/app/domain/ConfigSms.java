package cm.skysoft.app.domain;

import javax.persistence.*;

@Entity
@Table(name = "config_sms")
public class ConfigSms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_sms")
    private String userSms;

    @Column(name = "passwordSms")
    private String passwordSms;

    @Column(name = "sender_sms")
    private String senderSms;

    @Column(name = "nombre_chiffre_telephone")
    private Long nombreChiffreTelephone;

    @Column(name = "send_sms_notification")
    private Boolean sendSmsNotification = true;

    @Column(name = "send_sms_emission_suggestion")
    private Boolean sendSmsEmissionSugestion = true;

    @Column(name = "send_sms_validation_suggestion")
    private Boolean sendSmsValidationSugestion = true;

    @Column(name = "send_sms_non_validation_suggestion")
    private Boolean sendSmsNonValidationSugestion = true;

    @Column(name = "send_sms_execution_visite")
    private Boolean sendSmsExecutionVisite = true;

    @Column(name = "send_sms_modification_visite")
    private Boolean sendSmsModificationVisite = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserSms() {
        return userSms;
    }

    public void setUserSms(String userSms) {
        this.userSms = userSms;
    }

    public String getPasswordSms() {
        return passwordSms;
    }

    public void setPasswordSms(String passwordSms) {
        this.passwordSms = passwordSms;
    }

    public String getSenderSms() {
        return senderSms;
    }

    public void setSenderSms(String senderSms) {
        this.senderSms = senderSms;
    }

    public Long getNombreChiffreTelephone() {
        return nombreChiffreTelephone;
    }

    public void setNombreChiffreTelephone(Long nombreChiffreTelephone) {
        this.nombreChiffreTelephone = nombreChiffreTelephone;
    }

    public Boolean getSendSmsNotification() {
        return sendSmsNotification;
    }

    public void setSendSmsNotification(Boolean sendSmsNotification) {
        this.sendSmsNotification = sendSmsNotification;
    }

    public Boolean getSendSmsEmissionSugestion() {
        return sendSmsEmissionSugestion;
    }

    public void setSendSmsEmissionSugestion(Boolean sendSmsEmissionSugestion) {
        this.sendSmsEmissionSugestion = sendSmsEmissionSugestion;
    }

    public Boolean getSendSmsValidationSugestion() {
        return sendSmsValidationSugestion;
    }

    public void setSendSmsValidationSugestion(Boolean sendSmsValidationSugestion) {
        this.sendSmsValidationSugestion = sendSmsValidationSugestion;
    }

    public Boolean getSendSmsNonValidationSugestion() {
        return sendSmsNonValidationSugestion;
    }

    public void setSendSmsNonValidationSugestion(Boolean sendSmsNonValidationSugestion) {
        this.sendSmsNonValidationSugestion = sendSmsNonValidationSugestion;
    }

    public Boolean getSendSmsExecutionVisite() {
        return sendSmsExecutionVisite;
    }

    public void setSendSmsExecutionVisite(Boolean sendSmsExecutionVisite) {
        this.sendSmsExecutionVisite = sendSmsExecutionVisite;
    }

    public Boolean getSendSmsModificationVisite() {
        return sendSmsModificationVisite;
    }

    public void setSendSmsModificationVisite(Boolean sendSmsModificationVisite) {
        this.sendSmsModificationVisite = sendSmsModificationVisite;
    }

    @Override
    public String toString() {
        return "ConfigSms{" +
                "id=" + id +
                ", userSms='" + userSms + '\'' +
                ", passwordSms='" + passwordSms + '\'' +
                ", senderSms='" + senderSms + '\'' +
                ", nombreChiffreTelephone=" + nombreChiffreTelephone +
                ", sendSmsNotification=" + sendSmsNotification +
                ", sendSmsEmissionSugestion=" + sendSmsEmissionSugestion +
                ", sendSmsValidationSugestion=" + sendSmsValidationSugestion +
                ", sendSmsNonValidationSugestion=" + sendSmsNonValidationSugestion +
                ", sendSmsExecutionVisite=" + sendSmsExecutionVisite +
                ", sendSmsModificationVisite=" + sendSmsModificationVisite +
                '}';
    }
}
