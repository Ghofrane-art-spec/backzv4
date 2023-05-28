package EMC.Web.emc.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class Banque_couverture extends Banque implements Serializable {

	public Banque_couverture() {
		super();

	}

	public Banque_couverture(Long codeBanque, String nomBanque, String swiftBanque, Long tarifBanque, Long fraiBanque,
			Long codePays, List<Cheque> cheque) {
		super(codeBanque, nomBanque, swiftBanque, tarifBanque, fraiBanque, codePays, cheque);
	}

	@Override
	public String toString() {
		return "Banque_couverture [getCodeBanque()=" + getCodeBanque() + ", getNomBanque()=" + getNomBanque()
				+ ", getSwiftBanque()=" + getSwiftBanque() + ", getTarifBanque()=" + getTarifBanque()
				+ ", getFraiBanque()=" + getFraiBanque() + ", getCodePays()=" + getCodePays() + ", getCheque()="
				+ getCheque() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}
	
	

	



	

}
