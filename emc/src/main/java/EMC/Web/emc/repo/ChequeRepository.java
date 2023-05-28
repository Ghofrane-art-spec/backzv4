package EMC.Web.emc.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import EMC.Web.emc.entities.Cheque;

public interface ChequeRepository extends JpaRepository<Cheque, Long> {
	

}
