package EMC.Web.emc.entities;

import java.util.List;

import javax.persistence.Entity;


@Entity
public class Banque_destinataire extends Banque{
	private String nomTireur;
	private String adresse;
	public Banque_destinataire(Long codeBanque, String nomBanque, String swiftBanque, Long tarifBanque, Long fraiBanque,
			Long codePays, List<Cheque> cheque, String nomTireur, String adresse) {
		super(codeBanque, nomBanque, swiftBanque, tarifBanque, fraiBanque, codePays, cheque);
		this.nomTireur = nomTireur;
		this.adresse = adresse;
	}
	public Banque_destinataire() {
		super();
	}
	public String getNomTireur() {
		return nomTireur;
	}
	public void setNomTireur(String nomTireur) {
		this.nomTireur = nomTireur;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	@Override
	public String toString() {
		return "Banque_destinataire [nomTireur=" + nomTireur + ", adresse=" + adresse + ", getCodeBanque()="
				+ getCodeBanque() + ", getNomBanque()=" + getNomBanque() + ", getSwiftBanque()=" + getSwiftBanque()
				+ ", getTarifBanque()=" + getTarifBanque() + ", getFraiBanque()=" + getFraiBanque() + ", getCodePays()="
				+ getCodePays() + ", getCheque()=" + getCheque() + ", toString()=" + super.toString() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + "]";
	}
	
	
	
	

}
