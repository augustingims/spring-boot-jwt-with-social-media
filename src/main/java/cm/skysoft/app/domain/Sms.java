package cm.skysoft.app.domain;

import cm.skysoft.app.utils.MethoUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A Sms.
 */
@Entity
@Table(name = "sms")
public class Sms implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "message")
	private String message;

	@Column(name = "date_enregistrement")
	private LocalDateTime dateEnregistrement;

	@Column(name = "date_envoi")
	private LocalDateTime dateEnvoi;

	@Column(name = "envoyer")
	private Boolean envoyer = false;

	@Column(name = "sender")
	private String sender;

	@Column(name = "page")
	private Integer page;

	@Column(name = "quantite_initiale")
	private Integer quantiteInitiale;

	@Column(name = "quantite_fin")
	private Integer quantiteFin;

	@Column(name = "destinataire")
	private String destinataire;

	@Column(name = "agent")
	private String agent;

	@Column(name = "ssid_sms")
	private String ssidSms;

	@Column(name = "operator")
	private String operator;

	@Column(name = "cout")
	private Integer cout;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public Sms message(String message) {
		this.message = message;
		return this;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getDateEnregistrement() {
		return dateEnregistrement;
	}

	public Sms dateEnregistrement(LocalDateTime dateEnregistrement) {
		this.dateEnregistrement = dateEnregistrement;
		return this;
	}

	public void setDateEnregistrement(LocalDateTime dateEnregistrement) {
		this.dateEnregistrement = dateEnregistrement;
	}

	public LocalDateTime getDateEnvoi() {
		return dateEnvoi;
	}

	public Sms dateEnvoi(LocalDateTime dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
		return this;
	}

	public void setDateEnvoi(LocalDateTime dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
	}

	public Boolean isEnvoyer() {
		return envoyer;
	}

	public Sms envoyer(Boolean envoyer) {
		this.envoyer = envoyer;
		return this;
	}

	public void setEnvoyer(Boolean envoyer) {
		this.envoyer = envoyer;
	}

	public String getSender() {
		return sender;
	}

	public Sms sender(String sender) {
		this.sender = sender;
		return this;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Integer getPage() {
		return page;
	}

	public Sms page(Integer page) {
		this.page = page;
		return this;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getQuantiteInitiale() {
		return quantiteInitiale;
	}

	public Sms quantiteInitiale(Integer quantiteInitiale) {
		this.quantiteInitiale = quantiteInitiale;
		return this;
	}

	public void setQuantiteInitiale(Integer quantiteInitiale) {
		this.quantiteInitiale = quantiteInitiale;
	}

	public Integer getQuantiteFin() {
		return quantiteFin;
	}

	public Sms quantiteFin(Integer quantiteFin) {
		this.quantiteFin = quantiteFin;
		return this;
	}

	public void setQuantiteFin(Integer quantiteFin) {
		this.quantiteFin = quantiteFin;
	}

	public String getDestinataire() {
		return destinataire;
	}

	public Sms destinataire(String destinataire) {
		this.destinataire = destinataire;
		return this;
	}

	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public Boolean getEnvoyer() {
		return envoyer;
	}

	public Integer getCout() {
		return cout;
	}

	public Sms cout(Integer cout) {
		this.cout = cout;
		return this;
	}

	public void setCout(Integer cout) {
		this.cout = cout;
	}

	public String getSsidSms() {
		return ssidSms;
	}

	public void setSsidSms(String ssidSms) {
		this.ssidSms = ssidSms;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDateenvoiText() {

		return dateEnvoi != null ? MethoUtils.getDateTimeToString(dateEnvoi)
				: "-";
	}

	public String getDateEnregistrementText() {

		return dateEnregistrement != null ? MethoUtils
				.getDateTimeToString(dateEnregistrement) : "-";
	}

	public String getStatut() {

		String text = "";

		if (envoyer) {

			text = "Envoye";

		} else {

			text = "Non Envoye";

		}

		return text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Sms sms = (Sms) o;
		if (sms.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), sms.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Sms [id=" + id + ", message=" + message
				+ ", dateEnregistrement=" + dateEnregistrement + ", dateEnvoi="
				+ dateEnvoi + ", envoyer=" + envoyer + ", sender=" + sender
				+ ", page=" + page + ", quantiteInitiale=" + quantiteInitiale
				+ ", quantiteFin=" + quantiteFin + ", destinataire="
				+ destinataire + ", agent=" + agent + ", ssidSms=" + ssidSms
				+ ", operator=" + operator + ", cout=" + cout + "]";
	}

}
