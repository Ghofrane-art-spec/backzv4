package EMC.Web.emc.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;



@Entity
public class Cheque implements Serializable{
	@Id
	private Long numCheque;
	private Float montant;
	private String devise;
	private Date dateSaisie;
	private Date dateSortie;
	private Date dateReceptionRejet;
	private Long numBordereau;
	private Date dateReception;
	private String motifRejet;
	private Date dateRejet;
	private String image ;
	private String couleur;
	
	@Enumerated(EnumType.STRING)
	private StatutCheque statut;


	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, 
			property  = "numBordereau", 
			scope     = Long.class)
	@JsonIgnoreProperties(value = {"listeCh","montant"}, allowSetters = true)
	@ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH})
	@JoinColumn(name="bordereau_id")
	private Bordereau bordereau;
	
	@JsonBackReference(value="client-cheque")
	@ManyToOne(cascade = {
	    CascadeType.MERGE,
	    CascadeType.REFRESH})
	private Client clientCh;


	@JsonBackReference(value="banque-cheque")
	@ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH})
	private Banque banque;
	


	public Cheque(Long numCheque, Float montant, String devise, Date dateSaisie, Date dateSortie, Long numBordereau,
			Date dateReception, String motifRejet, Date dateRejet, StatutCheque statut, Bordereau bordereau,
			Client clientCh, Banque banque) {
		super();
		this.numCheque = numCheque;
		this.montant = montant;
		this.devise = devise;
		this.dateSaisie = dateSaisie;
		this.dateSortie = dateSortie;
		this.numBordereau = numBordereau;
		this.dateReception = dateReception;
		this.motifRejet = motifRejet;
		this.dateRejet = dateRejet;
		this.statut = statut;
		this.bordereau = bordereau;
		this.clientCh = clientCh;
		this.banque = banque;
	}
	

	public Cheque() {
		super();
	}
	
	public String getCouleur() {
		return couleur;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}

	public Date getDateReceptionRejet() {
		return dateReceptionRejet;
	}


	public void setDateReceptionRejet(Date dateReceptionRejet) {
		this.dateReceptionRejet = dateReceptionRejet;
	}

	public Long getNumCheque() {
		return numCheque;
	}

	public void setNumCheque(Long numCheque) {
		this.numCheque = numCheque;
	}

	public Float getMontant() {
		return montant;
	}

	public void setMontant(Float montant) {
		this.montant = montant;
	}
	public String getMotifRejet() {
		return motifRejet;
	}

	public void setMotifRejet(String motifRejet) {
		this.motifRejet = motifRejet;
	}


	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public Date getDateSaisie() {
		return dateSaisie;
	}

	public void setDateSaisie(Date dateSaisie) {
		this.dateSaisie = dateSaisie;
	}

	public Date getDateSortie() {
		return dateSortie;
	}

	public void setDateSortie(Date dateSortie) {
		this.dateSortie = dateSortie;
	}

	public Long getNumBordereau() {
		return numBordereau;
	}

	public void setNumBordereau(Long numBordereau) {
		this.numBordereau = numBordereau;
	}

	public Date getDateReception() {
		return dateReception;
	}

	public void setDateReception(Date dateReception) {
		this.dateReception = dateReception;
	}

	public Date getDateRejet() {
		return dateRejet;
	}

	public void setDateRejet(Date dateRejet) {
		this.dateRejet = dateRejet;
	}

	public StatutCheque getStatut() {
		return statut;
	}

	public void setStatut(StatutCheque statut) {
		this.statut = statut;
	}

	public Bordereau getBordereau() {
		return bordereau;
	}

	public void setBordereau(Bordereau bordereau) {
		this.bordereau = bordereau;
	}

	public Client getClientCh() {
		return clientCh;
	}

	public void setClientCh(Client clientCh) {
		this.clientCh = clientCh;
	}

	public Banque getBanque() {
		return banque;
	}

	public void setBanque(Banque banque) {
		this.banque = banque;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	@Override
	public String toString() {
		return "Cheque [numCheque=" + numCheque + ", montant=" + montant + ", devise=" + devise + ", dateSaisie="
				+ dateSaisie + ", dateSortie=" + dateSortie + ", dateReceptionRejet=" + dateReceptionRejet
				+ ", numBordereau=" + numBordereau + ", dateReception=" + dateReception + ", motifRejet=" + motifRejet
				+ ", dateRejet=" + dateRejet + ", image=" + image + ", couleur=" + couleur + ", statut=" + statut
				+ ", bordereau=" + bordereau + ", clientCh=" + clientCh + ", banque=" + banque + "]";
	}


	



	
	
	
}
