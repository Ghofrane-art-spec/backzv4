package EMC.Web.emc.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Compte implements Serializable{
	@Id
	private Long numCompte;
	private Long rib;
	
	@JsonManagedReference(value="client-compte")
	@OneToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH})
	private Client client;

	public Compte(Long numCompte, Long rib, Client client) {
		super();
		this.numCompte = numCompte;
		this.rib = rib;
		this.client = client;
	}

	public Compte() {
		super();
	}

	public Long getNumCompte() {
		return numCompte;
	}

	public void setNumCompte(Long numCompte) {
		this.numCompte = numCompte;
	}

	public Long getRib() {
		return rib;
	}

	public void setRib(Long rib) {
		this.rib = rib;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
	    return "Compte [numCompte=" + numCompte + ", rib=" + rib + ", client=" + client.getNomClient() + " " + client.getPrenomClient() + "]";
	}

	
	
	
	
	
	

}
