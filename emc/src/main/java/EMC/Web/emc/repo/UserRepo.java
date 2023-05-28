package EMC.Web.emc.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import EMC.Web.emc.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.matricule = :matricule")
    Optional<User> findMatricule(@Param("matricule") Long matricule);
    
    @Query("SELECT u FROM User u WHERE u.pwd = :pwd")
    Optional<User> findPwd(@Param("pwd")String pwd);
    
    @Query("SELECT u FROM User u WHERE u.matricule = :matricule AND u.pwd = :pwd")
    Optional<User> findUser(@Param("matricule")Long matricule, @Param("pwd")String pwd);
}
