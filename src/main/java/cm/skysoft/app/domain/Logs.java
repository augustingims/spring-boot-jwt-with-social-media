package cm.skysoft.app.domain;

import cm.skysoft.app.utils.MethoUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A Logs.
 */
@Entity
@Table(name = "logs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Logs implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
	private User user;

	@Column(name = "dateoperation")
	private LocalDateTime dateoperation;

	@Column(name = "libelle")
	private String libelle;

	@Column(name = "machine")
	private String machine;

	@Column(name = "ipmachine")
	private String ipmachine;

	@Column(name = "typelog")
	private Integer typelog;

	@Column(name = "type")
	private String type;

	public static String Web = "Web";
	public static String Mobile = "Mobile";

	public static int ENREGISTREMENT_USER = 1;
	public static int CONNEXIONSYSTEME = 1;
	public static int DECONNEXIONSYSTEME = 2;
	public static int CREATION_VISITE = 3;
	public static int ATTRIBUTION_VISITE = 4;
	public static int EMMETRE_SUGGESTION = 5;
	public static int EXECUTION_VISITE = 6;
	public static int ARCHIVAGE_VISITE = 7;
	public static int CREATION_RAPPORT_VISITE = 8;
	public static int CREATION_ARCHIVAGE_VISITE = 9;
	public static int PREPARATION_VISITE = 10;
	public static int CREATION_VISITE_BUREAU = 11;
	public static int VALIDATION_VISITE = 12;
	public static int ENREGISTREMENT_NOTIFICATION = 13;
	public static int ENREGISTREMENT_PARTICIPANT = 14;

	public Logs() {
		super();
	}

	public Logs(String libelle) {


		this.libelle = libelle;

		// user = Main.user;

	}

	public Logs(User user, String libelle, int type) {


		this.libelle = libelle;

		this.user = user;

		this.typelog = type;

		dateoperation = LocalDateTime.now();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public Logs user(User user) {
		this.user = user;
		return this;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getDateoperation() {
		return dateoperation;
	}

	public Logs dateoperation(LocalDateTime dateoperation) {
		this.dateoperation = dateoperation;
		return this;
	}

	public void setDateoperation(LocalDateTime dateoperation) {
		this.dateoperation = dateoperation;
	}

	public String getLibelle() {
		return libelle;
	}

	public Logs libelle(String libelle) {
		this.libelle = libelle;
		return this;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getMachine() {
		return machine;
	}

	public Logs machine(String machine) {
		this.machine = machine;
		return this;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getIpmachine() {
		return ipmachine;
	}

	public Logs ipmachine(String ipmachine) {
		this.ipmachine = ipmachine;
		return this;
	}

	public void setIpmachine(String ipmachine) {
		this.ipmachine = ipmachine;
	}

	public Integer getTypelog() {
		return typelog;
	}

	public Logs typelog(Integer typelog) {
		this.typelog = typelog;
		return this;
	}

	public String getDateOperationText() {
		return MethoUtils.getLocalDateTimeToString(dateoperation);
	}

	public void setTypelog(Integer typelog) {
		this.typelog = typelog;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Logs logs = (Logs) o;
		if (logs.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, logs.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Logs [id=" + id + ", user=" + user + ", dateoperation="
				+ dateoperation + ", libelle=" + libelle
				+ ", machine=" + machine + ", ipmachine=" + ipmachine
				+ ", typelog=" + typelog + "]";
	}

}
