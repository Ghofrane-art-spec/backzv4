package EMC.Web.emc.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference; 


@Table
@Entity
public class User implements Serializable {
	@Id
	private Long matricule;
    private String pwd;
    private String nom;
	private String prenom;
	private String email;
	private Long telephone;
    @Enumerated(EnumType.STRING)
    private Role role;
 
    @JsonBackReference(value="client-user")
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Client client;
    
	@ManyToOne(cascade = CascadeType.ALL)
	private Agence agence;

	public User(Long matricule, String pwd, String nom, String prenom, String email, Long telephone, Role role,
		 Client client, Agence agence) {
		super();
		this.matricule = matricule;
		this.pwd = pwd;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.role = role;
		this.client = client;
		this.agence = agence;
	}

	public User() {
		super();
	}

	public Long getMatricule() {
		return matricule;
	}

	public void setMatricule(Long matricule) {
		this.matricule = matricule;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getTelephone() {
		return telephone;
	}

	public void setTelephone(Long telephone) {
		this.telephone = telephone;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Agence getAgence() {
		return agence;
	}

	public void setAgence(Agence agence) {
		this.agence = agence;
	}

	@Override
	public String toString() {
		return "User [matricule=" + matricule + ", pwd=" + pwd + ", nom=" + nom + ", prenom=" + prenom + ", email="
				+ email + ", telephone=" + telephone + ", role=" + role +  ", client=" + client
				+ ", agence=" + agence + "]";
	}

	

	

}
	

