package EMC.Web.emc.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
public class Agence implements Serializable{
	@Id
	private Long codeAgence;
	private String nomAgence;
	private String adresse;
	private Long telephone;
	
	
	@OneToMany(cascade = CascadeType.ALL)
    private Set<User> users;
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, 
			property  = "id", 
			scope     = Long.class)
	@JsonManagedReference(value="client-agence")
	@OneToMany(mappedBy = "agence", cascade = CascadeType.ALL)
	private List<Client> clients;


	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Long getTelephone() {
		return telephone;
	}

	public void setTelephone(Long telephone) {
		this.telephone = telephone;
	}

	public Agence(Long codeAgence, String nomAgence, String adresse, Long telephone, Set<User> users,
			List<Client> clients) {
		super();
		this.codeAgence = codeAgence;
		this.nomAgence = nomAgence;
		this.adresse = adresse;
		this.telephone = telephone;
		this.users = users;
		this.clients = clients;
	}

	public Agence() {
		super();
	}

	public Long getCodeAgence() {
		return codeAgence;
	}

	public void setCodeAgence(Long codeAgence) {
		this.codeAgence = codeAgence;
	}

	public String getNomAgence() {
		return nomAgence;
	}

	public void setNomAgence(String nomAgence) {
		this.nomAgence = nomAgence;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	@Override
	public String toString() {
		return "Agence [ codeAgence=" + codeAgence + ", nomAgence=" + nomAgence + ", adresse=" + adresse
				+ ", telephone=" + telephone + ", users=" + users + ", clients=" + clients + "]";
	}

	

	
	

	
	

}
