package EMC.Web.emc.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
public class Bordereau implements Serializable {
	@Id
	private Long numBordereau;
	private Date dateBordereau;
	
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, 
property  = "numCheque", 
scope     = Long.class)
	@JsonIgnoreProperties(value = {"listeCh","montant"}, allowSetters = true)
	@OneToMany(mappedBy = "bordereau",cascade = CascadeType.ALL)
	private List<Cheque> listeCh;
	
	
	
	public Bordereau(Long numBordereau, Date dateBordereau, List<Cheque> listeCh) {
		super();
		this.numBordereau = numBordereau;
		this.dateBordereau = dateBordereau;
		this.listeCh = listeCh;
	}
	public Bordereau() {
		super();
	}
	public Long getNumBordereau() {
		return numBordereau;
	}
	public void setNumBordereau(Long numBordereau) {
		this.numBordereau = numBordereau;
	}
	public Date getDateBordereau() {
		return dateBordereau;
	}
	public void setDateBordereau(Date dateBordereau) {
		this.dateBordereau = dateBordereau;
	}
	public List<Cheque> getListeCh() {
		return this.listeCh;
	}
	public void setListeCh(List<Cheque> listeCh) {
		this.listeCh = listeCh;
	}
	@Override
	public String toString() {
		return "Bordereau [numBordereau=" + numBordereau + ", dateBordereau=" + dateBordereau + ", ListeCh=" + getListeCh()
				+ "]";
	}
	
}
