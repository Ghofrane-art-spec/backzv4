package EMC.Web.emc.controlleur;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import EMC.Web.emc.entities.Agence;
import EMC.Web.emc.entities.Client;
import EMC.Web.emc.entities.Compte;
import EMC.Web.emc.entities.Role;
import EMC.Web.emc.entities.User;
import EMC.Web.emc.repo.AgenceRepository;
import EMC.Web.emc.repo.ClientRepo;
import EMC.Web.emc.repo.CompteRepo;
import EMC.Web.emc.service.ChequeBordService;
import EMC.Web.emc.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class user_controller {
	@Autowired
	private UserService us;
	@Autowired
	private ClientRepo clientR;
	@Autowired
	private AgenceRepository repoAgence;
	
	@Autowired
	private ChequeBordService service;
	
	@Autowired
	private CompteRepo RepoCompte;
	
	
	@GetMapping("/login/{matricule}/{pwd}")
	public ResponseEntity<User> login(@PathVariable("matricule")Long matricule,@PathVariable("pwd")String pwd){
		Integer t=us.UserExists(matricule, pwd);
		if(t==0) {
			User user=us.FindUser(matricule, pwd);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		else if(t==1) {
			User user=us.FindUser(matricule, pwd);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		else {
			User u=us.FindMatricule(matricule);
			return new ResponseEntity<>(u, HttpStatus.OK);
		}
			
	}
	
	@GetMapping("/iden/{matricule}/{pwd}")
	public ResponseEntity<Integer> identifier(@PathVariable("matricule")Long matricule,@PathVariable("pwd")String pwd){
		Integer t=us.UserExists(matricule, pwd);
		return new ResponseEntity<>(t, HttpStatus.OK);
		
	}
	
	//espace client
	@GetMapping("/retournerClient/{matricule}")
	public ResponseEntity<Client> espaceClient(@PathVariable("matricule")Long matricule){
		User user=us.FindMatricule(matricule);
		Client client=user.getClient();
		return new ResponseEntity<>(client,HttpStatus.OK);
		
	}
	
	@GetMapping("/retournerCompte/{id}")
	public ResponseEntity<Compte> espaceCompte(@PathVariable("id")Long id){
		Client client=clientR.findById(id).orElse(null) ;
		Compte compte=client.getCompte();
		return new ResponseEntity<>(compte,HttpStatus.OK);
		
	}
	
	//admin-user
	
	@GetMapping("/afficherUsers")
	public ResponseEntity<List<User>> AffichertoutUsers(){
		List<User> liste=us.afficherUsers();
		return new ResponseEntity<>(liste, HttpStatus.OK);
	}
	
	@DeleteMapping("/supprimerUser")
    public ResponseEntity<?> supprimerUser(@RequestBody User user) {
       us.supprimerUser(user);
       return new ResponseEntity<>(HttpStatus.OK);
    }
	
	//modifier user
	@GetMapping("/Usermodif/{pwd}/{nom}/{prenom}/{email}/{numtel}")
	public ResponseEntity<User> modifierUser(@PathVariable("pwd")String pwd,@PathVariable("nom")String nom,@PathVariable("prenom")String prenom,@PathVariable("email")String email,@PathVariable("numtel")Long numtel){
		User user=new User();
		user.setMatricule(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
		user.setPwd(pwd);
		user.setNom(nom);
		user.setPrenom(prenom);
		user.setEmail(email);
		user.setTelephone(numtel);
		return new ResponseEntity<>(user, HttpStatus.OK);
		
	}
	
	@PutMapping("/enregistrerModif/{matricule}")
	public ResponseEntity<User> enregistrerModif(@PathVariable("matricule")Long matricule,@RequestBody User userr){
		User user=us.FindMatricule(matricule);
		user.setPwd(userr.getPwd());
		user.setNom(userr.getNom());
		user.setPrenom(userr.getPrenom());
		user.setEmail(userr.getEmail());
		user.setTelephone(userr.getTelephone());
		us.enregistrer(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
		
	}
	
	//ajouter user
	
	
	@GetMapping("/creeUser/{matricule}/{pwd}/{nom}/{prenom}/{email}/{numtel}/{role}")
	public ResponseEntity<User> creeUser(@PathVariable("matricule")Long matricule,@PathVariable("pwd")String pwd,@PathVariable("nom")String nom,@PathVariable("prenom")String prenom,@PathVariable("email")String email,@PathVariable("numtel")Long numtel,@PathVariable("role")String role){
		Role r = Role.fromString(role);
		User u=us.FindMatricule(matricule);
		if(u!=null) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}else {
			User user=new User(matricule,pwd,nom,prenom,email,numtel,r,null,null);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		
	}
	
	
	@PostMapping("/enregistrerUser/{codeAgence}")
	public ResponseEntity<User> enregistrerUser(@PathVariable("codeAgence")Long codeAgence,@RequestBody User user){
		if(repoAgence.existsById(codeAgence)) {
			Agence agence=repoAgence.findById(codeAgence).orElse(null);
			user.setAgence(agence);
			us.enregistrer(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
		
		
	}
	
	@PostMapping("/UserClient/{numClient}")
	public ResponseEntity<Client> UserClient(@PathVariable("numClient")Long numClient,@RequestBody User user){
		Client c=clientR.findClient(numClient).orElse(null);
		Client client=new Client();
		client.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
		client.setNumClient(numClient);
		client.setUser(user);
		client.setMailClient(user.getEmail());
		client.setNomClient(user.getNom());
		client.setNumeroTelephone(user.getTelephone());
		client.setPrenomClient(user.getPrenom());
		client.setAgence(user.getAgence());
		clientR.save(client);
		user.setClient(client);
		us.enregistrer(user);
		return new ResponseEntity<>(client, HttpStatus.OK);}
		
		
	
	@GetMapping("/compte/{numCompte}/{rib}")
	 public ResponseEntity<Compte> creeCompte(@PathVariable("numCompte")Long numCompte,@PathVariable("rib")Long rib) {
		Compte c=RepoCompte.findById(numCompte).orElse(null);
		if(c!=null) {
			return new ResponseEntity<>(null,HttpStatus.OK);
		}else {
			Compte compte=new Compte(numCompte,rib,null);
			return new ResponseEntity<>(compte,HttpStatus.OK);
		}
	}
	
	
	@PostMapping("/enregistrerCompte")
	public ResponseEntity<Compte> enregistrerUser(@RequestBody Compte compte){
		service.enregistrerCompte(compte);
		return new ResponseEntity<>(compte, HttpStatus.OK);
		
	}
	
	@PutMapping("/relierCompteClient/{matricule}")
	public ResponseEntity<Compte> enregistrerCompte(@PathVariable("matricule")Long matricule,@RequestBody Compte compte){
		User user=us.FindMatricule(matricule);
		Client client=user.getClient();
		client.setCompte(compte);
		compte.setClient(client);
		service.enregistrerCompte(compte);
		service.enregistrerClient(client);
		return new ResponseEntity<>(compte, HttpStatus.OK);
		
	}
	
	@GetMapping("/creeAgence/{codeAgence}")
	public ResponseEntity<Agence> creeAgence(@PathVariable("codeAgence")Long codeAgence,@PathVariable("nomAgence")String nomAgence,
			@PathVariable("adresse")String adresse,@PathVariable("telephone")Long telephone){
		Agence agence=new Agence(codeAgence,nomAgence,adresse,telephone,null,null);
		return new ResponseEntity<>(agence, HttpStatus.OK);
	}
	
	
	@PostMapping("/enregistrerAgence")
	public ResponseEntity<Agence> enregistrerAgence(@RequestBody Agence agence){
		repoAgence.save(agence);
		return new ResponseEntity<>(agence, HttpStatus.OK);
		
	}
	
	@GetMapping("/user/{matricule}")
	public ResponseEntity<Boolean> user(@PathVariable("matricule")Long matricule){
		User user=us.FindMatricule(matricule);
		if(user==null) {
			return new ResponseEntity<>(false, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/compte/{numCompte}")
	public ResponseEntity<Boolean> compte(@PathVariable("numCompte")Long numCompte){
		Compte compte=service.rechercheCompte(numCompte);
		if(compte==null) {
			return new ResponseEntity<>(false, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/client/{numClient}")
	public ResponseEntity<Boolean> client(@PathVariable("numClient")Long numClient){
		Client client=service.rechercheClient(numClient);
		if(client==null) {
			return new ResponseEntity<>(false, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		
	}
	
	
	
	
	
	
	

}
