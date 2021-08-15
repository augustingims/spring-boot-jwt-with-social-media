package cm.skysoft.app.domain;

import javax.persistence.*;

@Entity
@Table(name = "config_application")
public class ConfigApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_config_application")
    private String codeConfigApplication;

    @Lob
    @Column(name = "pattern_emission_suggestion")
        private String patternEmissionSuggestion;

    @Lob
    @Column(name = "pattern_validated_suggestion")
    private String patternValidatedSuggestion;

    @Lob
    @Column(name = "pattern_not_validated_suggestion")
    private String patternNotValidatedSuggestion;

    @Lob
    @Column(name = "pattern_execution_visit")
    private String patternExecutionVisit;

    @Lob
    @Column(name = "pattern_validated_planned_visit")
    private String patternValidatedPlannedVisit;

    @Lob
    @Column(name = "pattern_validated_visit_with_participant")
    private String patternValidatedVisitWithParticipant;

    @Lob
    @Column(name = "pattern_update_planned_visit")
    private String patternUpdatePlannedVisit;


    @Column(name = "link_behavior_forcage")
    private String linkBehaviorForage;

    @Column(name = "user_sms")
    private String userSms;

    @Column(name = "passwordSms")
    private String passwordSms;

    @Column(name = "sender_sms")
    private String senderSms;

    @Column(name = "nombre_chiffre_telephone")
    private Long nombreChiffreTelephone;

    @Column(name = "send_sms")
    private Boolean sendSms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatternEmissionSuggestion() {
        return patternEmissionSuggestion;
    }

    public void setPatternEmissionSuggestion(String patternEmissionSuggestion) {
        this.patternEmissionSuggestion = patternEmissionSuggestion;
    }

    public String getPatternValidatedSuggestion() {
        return patternValidatedSuggestion;
    }

    public void setPatternValidatedSuggestion(String patternValidatedSuggestion) {
        this.patternValidatedSuggestion = patternValidatedSuggestion;
    }

    public String getPatternNotValidatedSuggestion() {
        return patternNotValidatedSuggestion;
    }

    public void setPatternNotValidatedSuggestion(String patternNotValidatedSuggestion) {
        this.patternNotValidatedSuggestion = patternNotValidatedSuggestion;
    }

    public String getPatternExecutionVisit() {
        return patternExecutionVisit;
    }

    public void setPatternExecutionVisit(String patternExecutionVisit) {
        this.patternExecutionVisit = patternExecutionVisit;
    }

    public String getPatternValidatedPlannedVisit() {
        return patternValidatedPlannedVisit;
    }

    public void setPatternValidatedPlannedVisit(String patternValidatedPlannedVisit) {
        this.patternValidatedPlannedVisit = patternValidatedPlannedVisit;
    }

    public String getCodeConfigApplication() {
        return codeConfigApplication;
    }

    public void setCodeConfigApplication(String codeConfigApplication) {
        this.codeConfigApplication = codeConfigApplication;
    }

    public String getPatternValidatedVisitWithParticipant() {
        return patternValidatedVisitWithParticipant;
    }

    public void setPatternValidatedVisitWithParticipant(String patternValidatedVisitWithParticipant) {
        this.patternValidatedVisitWithParticipant = patternValidatedVisitWithParticipant;
    }

    public String getPatternUpdatePlannedVisit() {
        return patternUpdatePlannedVisit;
    }

    public void setPatternUpdatePlannedVisit(String patternUpdatePlannedVisit) {
        this.patternUpdatePlannedVisit = patternUpdatePlannedVisit;
    }


    public String getLinkBehaviorForage() {
        return linkBehaviorForage;
    }

    public void setLinkBehaviorForage(String linkBehaviorForage) {
        this.linkBehaviorForage = linkBehaviorForage;
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

    public Boolean getSendSms() {
        return sendSms;
    }

    public void setSendSms(Boolean sendSms) {
        this.sendSms = sendSms;

    }
}
