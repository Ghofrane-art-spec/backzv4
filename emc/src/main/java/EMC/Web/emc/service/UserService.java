package EMC.Web.emc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import EMC.Web.emc.entities.Cheque;
import EMC.Web.emc.entities.User;
import EMC.Web.emc.exception.UserNotFoundException;
import EMC.Web.emc.repo.UserRepo;

@Service
@Transactional
public class UserService {
	 private final UserRepo userRepo;
	    @Autowired
	    public UserService(UserRepo userRepo) {
	        this.userRepo = userRepo;
	    }
	    public Integer UserExists (Long matricule,String pwd) {
	    	if(userRepo.findMatricule(matricule).isPresent()) {
	    		if(userRepo.findUser(matricule,pwd).isPresent()) {
	    			return 0;
	    		}
	    		else return 1;
	    	}
	    	else {
	    		return 2;
	    	}
	    }
	    public User FindUser(Long matricule,String pwd) {
	    	return userRepo.findUser(matricule,pwd)
	    			.orElseThrow(() -> new UserNotFoundException("mot de passe incorrecte"));
	    }
	    public User FindMatricule(Long matricule) {
	    	return userRepo.findMatricule(matricule)
	    			.orElse(null);
	    }
	    
	    public List<User>afficherUsers(){
	    	List<User>liste=userRepo.findAll();
	    	return liste;
	    	}
	    
	    public void supprimerUser(User user){
		       Long matricule=user.getMatricule();
		       userRepo.deleteById(matricule);
		    }
	    
	    public void enregistrer(User user){
		       userRepo.save(user);
		    }
	    
	   
	  
	    
	    
	
		
		   
}
