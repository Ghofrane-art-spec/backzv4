package EMC.Web.emc.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import EMC.Web.emc.entities.Compte;

public interface CompteRepo extends JpaRepository<Compte, Long>{
	

}
