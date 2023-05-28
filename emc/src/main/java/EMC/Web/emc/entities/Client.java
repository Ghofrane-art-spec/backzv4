package EMC.Web.emc.entities;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class Client implements Serializable{
	
@Id
@GeneratedValue
private Long id;
private Long numClient;
private String nomClient;
private String prenomClient;
private Long numeroTelephone;
private String mailClient;

@JsonBackReference(value="client-compte")
@OneToOne(mappedBy = "client",cascade = CascadeType.ALL)
private Compte compte;

@JsonManagedReference(value="client-cheque")
@OneToMany(mappedBy = "clientCh", cascade = CascadeType.ALL)
private List<Cheque> cheques;

@JsonManagedReference(value="client-user")
@OneToOne(cascade = {
           CascadeType.MERGE,
           CascadeType.REFRESH})
private User user;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, 
property  = "codeAgence", 
scope     = Long.class)
@JsonBackReference(value="client-agence")
@ManyToOne(cascade = {
    CascadeType.MERGE,
    CascadeType.REFRESH})
private Agence agence;



public Client(Long numClient, String nomClient, String prenomClient, Long numeroTelephone, String mailClient,
		Compte compte, List<Cheque> cheques, User user, Agence agence) {
	super();
	this.numClient = numClient;
	this.nomClient = nomClient;
	this.prenomClient = prenomClient;
	this.numeroTelephone = numeroTelephone;
	this.mailClient = mailClient;
	this.compte = compte;
	this.cheques = cheques;
	this.user = user;
	this.agence = agence;
}



public Client() {
	super();
}



public Long getId() {
	return id;
}



public void setId(Long id) {
	this.id = id;
}



public Long getNumClient() {
	return numClient;
}



public void setNumClient(Long numClient) {
	this.numClient = numClient;
}



public String getNomClient() {
	return nomClient;
}



public void setNomClient(String nomClient) {
	this.nomClient = nomClient;
}



public String getPrenomClient() {
	return prenomClient;
}



public void setPrenomClient(String prenomClient) {
	this.prenomClient = prenomClient;
}



public Long getNumeroTelephone() {
	return numeroTelephone;
}



public void setNumeroTelephone(Long numeroTelephone) {
	this.numeroTelephone = numeroTelephone;
}



public String getMailClient() {
	return mailClient;
}



public void setMailClient(String mailClient) {
	this.mailClient = mailClient;
}



public Compte getCompte() {
	return compte;
}



public void setCompte(Compte compte) {
	this.compte = compte;
}



public List<Cheque> getCheques() {
	return cheques;
}



public void setCheques(List<Cheque> cheques) {
	this.cheques = cheques;
}



public User getUser() {
	return user;
}



public void setUser(User user) {
	this.user = user;
}



public Agence getAgence() {
	return agence;
}



public void setAgence(Agence agence) {
	this.agence = agence;
}



@Override
public String toString() {
	return "Client [id=" + id + ", numClient=" + numClient + ", nomClient=" + nomClient + ", prenomClient="
			+ prenomClient + ", numeroTelephone=" + numeroTelephone + ", mailClient=" + mailClient + ", compte="
			+ compte + ", cheques=" + cheques + ", user=" + user + ", agence=" + agence + "]";
}





}
