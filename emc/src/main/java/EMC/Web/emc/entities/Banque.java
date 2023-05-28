package EMC.Web.emc.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
public class Banque implements Serializable{
	@Id
	private Long codeBanque;
	private String nomBanque;
	private String swiftBanque;
	private Long tarifBanque;
	private Long fraiBanque;
	private Long codePays;
	@JsonManagedReference(value="banque-cheque")
	@OneToMany(mappedBy = "banque",cascade = CascadeType.ALL)
	private List<Cheque> cheque;
	public Banque(Long codeBanque, String nomBanque, String swiftBanque, Long tarifBanque, Long fraiBanque, Long codePays,
			List<Cheque> cheque) {
		super();
		this.codeBanque = codeBanque;
		this.nomBanque = nomBanque;
		this.swiftBanque = swiftBanque;
		this.tarifBanque = tarifBanque;
		this.fraiBanque = fraiBanque;
		this.codePays = codePays;
		this.cheque = cheque;
	}
	public Banque() {
		super();
	}
	public Long getCodeBanque() {
		return codeBanque;
	}
	public void setCodeBanque(Long codeBanque) {
		this.codeBanque = codeBanque;
	}
	public String getNomBanque() {
		return nomBanque;
	}
	public void setNomBanque(String nomBanque) {
		this.nomBanque = nomBanque;
	}
	public String getSwiftBanque() {
		return swiftBanque;
	}
	public void setSwiftBanque(String swiftBanque) {
		this.swiftBanque = swiftBanque;
	}
	public Long getTarifBanque() {
		return tarifBanque;
	}
	public void setTarifBanque(Long tarifBanque) {
		this.tarifBanque = tarifBanque;
	}
	public Long getFraiBanque() {
		return fraiBanque;
	}
	public void setFraiBanque(Long fraiBanque) {
		this.fraiBanque = fraiBanque;
	}
	public Long getCodePays() {
		return codePays;
	}
	public void setCodePays(Long codePays) {
		this.codePays = codePays;
	}
	public List<Cheque> getCheque() {
		return cheque;
	}
	public void setCheque(List<Cheque> cheque) {
		this.cheque = cheque;
	}
	@Override
	public String toString() {
		return "Banque [codeBanque=" + codeBanque + ", nomBanque=" + nomBanque + ", swiftBanque=" + swiftBanque
				+ ", tarifBanque=" + tarifBanque + ", fraiBanque=" + fraiBanque + ", codePays=" + codePays + ", cheque="
				+ cheque + "]";
	}
	
	

	

}
