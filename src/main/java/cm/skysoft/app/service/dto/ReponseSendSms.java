package cm.skysoft.app.service.dto;

/**
 * Created by francis on 03/03/21.
 */

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Transient;

public class ReponseSendSms {

	private String statut;
	private Long cout;
	private Double solde;
	private String description;
	private String ssidSms;
	private String operator;

	private static final String SUCCES = "1";

	private static final String ERROR_ENVOI = "0";

	public static final String COMPTE_INACTIF = "-1";

	public static final String SOLDE_INSUFFISANT = "-3";

	public static final String COMPTE_INEXISTANT = "-2";

	public static final String DESTINATAIRE_INCORRECT = "-4";

	public static final String API_NON_AUTORIE = "-5";

	public static final String COMPTE_EXPIRE = "-6";

	public static final String DATE_ENVOI_DEPASSEE = "-7";

	public static final String OPERATEUR_INDEFINI = "-8";

	public ReponseSendSms() {
		super();

		this.statut = ERROR_ENVOI;
		this.cout = 0L;
		this.description = "";
		this.ssidSms = "";
		this.solde = (double) 0;

	}

	public ReponseSendSms(String statut, Long cout, String description,
						  String ssidSms) {
		super();
		this.statut = statut;
		this.cout = cout;
		this.description = description;
		this.ssidSms = ssidSms;
		this.solde = (double) 0;
	}

	public ReponseSendSms(String statut, Long cout, String description,
						  String ssidSms, Double solde) {
		super();
		this.statut = statut;
		this.cout = cout;
		this.description = description;
		this.ssidSms = ssidSms;
		this.solde = solde;

	}

	public ReponseSendSms(String statut, Long cout, String description) {
		super();
		this.statut = statut;
		this.cout = cout;
		this.solde = (double) 0;
		this.description = description;
		this.ssidSms = "";
	}

	public ReponseSendSms(String statut, String description) {
		super();
		this.statut = statut;
		this.cout = 0L;
		this.solde = (double) 0;
		this.description = description;
		this.ssidSms = "";
	}

	public ReponseSendSms(String statut, String description, Double solde) {
		super();
		this.statut = statut;
		this.cout = 0L;
		this.solde = solde;
		this.description = description;
		this.ssidSms = "";
	}

	@Transient
	@JsonSerialize
	@JsonDeserialize
	public Boolean isSuccess() {

		return SUCCES.equals(statut);
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	private String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Long getCout() {
		return cout;
	}

	public void setCout(Long cout) {
		this.cout = cout;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSsidSms() {
		return ssidSms;
	}

	public void setSsidSms(String ssidSms) {
		this.ssidSms = ssidSms;
	}

	public Double getSolde() {
		return solde;
	}

	public void setSolde(Double solde) {
		this.solde = solde;
	}

	public void addSolde(Double solde) {
		this.solde += solde;
	}

	public void add(ReponseSendSms r) {

		if (!isSuccess()) {
			statut = r.getStatut();
		}

		cout += r.getCout();
		description = description.equals("") ? r.getDescription() : description;
		ssidSms = ssidSms.equals("") ? r.getSsidSms() : ssidSms;
		solde = r.getSolde();
	}

	@Override
	public String toString() {
		return "ReponseSendSms [statut=" + statut + ", cout=" + cout
				+ ", solde=" + solde + ", description=" + description
				+ ", ssid_sms=" + ssidSms + "]";
	}

	public String myToString() {
		return statut + ";" + cout + ";" + solde + ";" + description + ";"
				+ ssidSms;
	}

}
