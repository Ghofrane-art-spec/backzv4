package EMC.Web.emc.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import EMC.Web.emc.entities.Client;
import EMC.Web.emc.entities.User;

public interface ClientRepo extends JpaRepository<Client, Long>{

	 @Query("SELECT c FROM Client c WHERE c.numClient = :numClient")
	 Optional<Client> findClient(@Param("numClient")Long numClient);
}
