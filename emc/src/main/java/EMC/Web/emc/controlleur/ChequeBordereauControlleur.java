package EMC.Web.emc.controlleur;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import EMC.Web.emc.entities.Agence;
import EMC.Web.emc.entities.Banque;
import EMC.Web.emc.entities.Banque_couverture;
import EMC.Web.emc.entities.Banque_destinataire;
import EMC.Web.emc.entities.Bordereau;
import EMC.Web.emc.entities.Cheque;
import EMC.Web.emc.entities.Client;
import EMC.Web.emc.entities.Compte;
import EMC.Web.emc.entities.StatutCheque;
import EMC.Web.emc.entities.User;
import EMC.Web.emc.message.ResponseMessage;
import EMC.Web.emc.repo.AgenceRepository;
import EMC.Web.emc.repo.BanqueCouvertureRepo;
import EMC.Web.emc.repo.BanqueDestinataireRepo;
import EMC.Web.emc.repo.BanqueRepo;
import EMC.Web.emc.repo.BordereauRepository;
import EMC.Web.emc.repo.ChequeRepository;
import EMC.Web.emc.repo.ClientRepo;
import EMC.Web.emc.repo.CompteRepo;
import EMC.Web.emc.repo.FilesStorageService;
import EMC.Web.emc.service.ChequeBordService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ChequeBordereauControlleur {
	@Autowired
	ChequeBordService chequeBorSer;
	@Autowired
	ChequeRepository chequeRepo;
	@Autowired
	BordereauRepository borRepo;
	@Autowired
	BanqueRepo repobanque;
	@Autowired
	ClientRepo repoclient;
	@Autowired
	CompteRepo repocompte;
	@Autowired
	BanqueCouvertureRepo repoBanqueC;
	@Autowired
	BanqueDestinataireRepo repoBanqueD;
	@Autowired
	AgenceRepository agenceRepo;
	@Autowired
	private FilesStorageService storageService;
	@Autowired
	private ChequeRepository chequeRepository;
	
	
	
	//saisie de cheque

	@GetMapping("/saisieCh/{numCheque}/{montant}/{devise}/{numbordereau}")
	public ResponseEntity<Cheque> saisieCheque(@PathVariable("numCheque") Long numCheque, 
	                                        @PathVariable("montant") Float montant, 
	                                        @PathVariable("devise") String devise,@PathVariable("numbordereau") Long numBordereau) {
		Cheque c=chequeRepo.findById(numCheque).orElse(null);
	    if(c==null) {
	       Cheque ch = chequeBorSer.créerCheque(numCheque, montant, devise, null,numBordereau);
	       return new ResponseEntity<>(ch, HttpStatus.OK);}
	    else {
	       return new ResponseEntity<>(null, HttpStatus.OK);
	        }
	}

	

	@PostMapping(value="/saisieTest" ,consumes={"application/json"})
	public ResponseEntity<?> enregistrer(@RequestBody Cheque cheque) {
	    
	    try {
	    	Long numBordereau=cheque.getNumBordereau();
	    	Bordereau bordereau=chequeBorSer.créerBordereau(numBordereau, null);  
	    	if(bordereau.getListeCh() == null) {
	    		List<Cheque> listeCh = new ArrayList<>();
	    	    listeCh.add(cheque);
	    	    bordereau.setListeCh(listeCh);
	    	} else {
	    	    bordereau.getListeCh().add(cheque);
	    	}
	    	cheque.setDateSaisie(new Date());
	        borRepo.save(bordereau);

	        chequeRepo.save(cheque);
	        cheque.setBordereau(bordereau);
	        chequeRepo.save(cheque);
	        return new ResponseEntity<>(HttpStatus.CREATED);
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

			
	
	@GetMapping("/longueurCheque/{numCheque}")
	public ResponseEntity<Integer> longueurCheque(@PathVariable("numCheque")Long numCheque) {
		Integer t=chequeBorSer.LonguerCheque(numCheque);
		return new ResponseEntity<>(t, HttpStatus.OK);
	}
	
	
	@PostMapping("/uploadImgCheque/{id}")
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file , @PathVariable Long id) {
	    String message = "";
	    try {
	    	Cheque cheque = chequeRepository.getOne(id);
	     String fileName = storageService.store(file);
	     String chequeImgUrl =  ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/downloadChequeImg/")
	                .path(fileName)
	                .toUriString();
	     
	     String chequeImgUrl2=chequeImgUrl.replace("4200","8080");
	     cheque.setImage(chequeImgUrl2);
	     chequeRepository.save(cheque);

	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }

	
	@GetMapping("/nombrechequeBor/{numBordereau}")
	public ResponseEntity<Integer> nombrechequeBor(@PathVariable("numBordereau")Long numBordereau) {
		Bordereau bordereau=chequeBorSer.rechercheBordereau(numBordereau);
		if (bordereau == null) {
		    return new ResponseEntity<>(0, HttpStatus.OK);
		} else {
			Integer t=bordereau.getListeCh().size();
		    if (t < 6) {
		        return new ResponseEntity<>(1, HttpStatus.OK);
		    } else {
		        return new ResponseEntity<>(2, HttpStatus.OK);
		    }
		}

	}
	
	
	
	//Search
	
	@GetMapping("/rechercherCh/{numCheque}/{numBordereau}/{numCompte}")
	   public ResponseEntity<Cheque> rechercherCheque(@PathVariable("numCheque")Long numCheque,
			   @PathVariable("numBordereau")Long numBordereau,@PathVariable("numCompte")Long numCompte) {
		
		Cheque cheque1=chequeBorSer.RechercherCheque(numCheque);
		Cheque cheque2=chequeBorSer.rechercheChBordereau(numBordereau,numCheque);
		if(cheque1==null) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
		else if(cheque1.equals(cheque2)) {
			Cheque cheque3=chequeBorSer.rechercheChCompte(numCompte,numCheque);
			if(cheque1.equals(cheque3)) {
				return new ResponseEntity<>(cheque3, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(cheque1, HttpStatus.OK);
			}
			
		}else {
		return new ResponseEntity<>(cheque1, HttpStatus.OK);}
		
	}
	
	@GetMapping("/CompteExistant/{numCompte}")
	public ResponseEntity<Compte> CompteExistant(@PathVariable("numCompte")Long numCompte) {
		Compte test=chequeBorSer.rechercheCompte(numCompte);
		return new ResponseEntity<>(test, HttpStatus.OK);
	}
	
	@GetMapping("/BorExistant/{numBordereau}")
	public ResponseEntity<Bordereau> BorExistant(@PathVariable("numBordereau")Long numBordereau) {
		Bordereau test=chequeBorSer.rechercheBordereau(numBordereau);
		return new ResponseEntity<>(test, HttpStatus.OK);
	}
	
	@GetMapping("/chequeExistant/{numCheque}")
	public ResponseEntity<Boolean> chequeExistant(@PathVariable("numCheque")Long numCheque) {
		Boolean test=chequeBorSer.ChequeExistant(numCheque);
		return new ResponseEntity<>(test, HttpStatus.OK);
	}
	
	
	//finJournée
	//agence
	@GetMapping("/finJournee")
	   public ResponseEntity<List<Bordereau>> finJournée() {
		List<Bordereau> liste =chequeBorSer.afficherBordereau();
		return new ResponseEntity<>(liste, HttpStatus.OK);
	}

	@GetMapping("/nbrCheques")
	 public ResponseEntity<Integer> nbrCheques() {
		Integer nbrCh =chequeBorSer.NbrCheque();
		return new ResponseEntity<>(nbrCh, HttpStatus.OK);
	}
	
	@GetMapping("/nbrBordereau")
	 public ResponseEntity<Integer> nbrBordereau() {
		List<Bordereau> liste =chequeBorSer.afficherBordereau();
		return new ResponseEntity<>(liste.size(), HttpStatus.OK);
	}
	
	//service
	@GetMapping("/finJourneeService")
	   public ResponseEntity<List<Bordereau>> finJournéeService() {
		List<Bordereau> liste =chequeBorSer.nbrbordereauSerice();
		return new ResponseEntity<>(liste, HttpStatus.OK);
	}
	
	@GetMapping("/nbrChequeService")
	   public ResponseEntity<Integer> nbrChequeService() {
		List<Cheque> liste =chequeBorSer.nbrchequeservice();
		return new ResponseEntity<>(liste.size(), HttpStatus.OK);
	}
	
	@GetMapping("/nbrBordereauService")
	   public ResponseEntity<Integer> nbrBordereauService() {
		List<Bordereau> liste =chequeBorSer.nbrbordereauSerice();
		return new ResponseEntity<>(liste.size(), HttpStatus.OK);
	}

	
	
	
	//service encaissement
	//en route
	@GetMapping("/listeCheques")
	 public ResponseEntity<List<Cheque>> listCheques() {
		List<Cheque> liste =chequeBorSer.afficherCheques();
		return new ResponseEntity<>(liste, HttpStatus.OK);
	}
	
	//ouverture update
	
	
	@GetMapping("/creeClient/{nomClient}/{numCompte}")
	 public ResponseEntity<Client> CreeClient(@PathVariable("nomClient")String nomClient,@PathVariable("numCompte")Long numCompte) {
		Compte c=chequeBorSer.trouverCompte(numCompte);
		if(c==null) {
			Client client = new Client();
			client.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
			client.setNomClient(nomClient);
			return new ResponseEntity<>(client,HttpStatus.OK);
		}
		else {
			Client client=c.getClient();
			return new ResponseEntity<>(client,HttpStatus.OK);
		}
	}

	
	@PutMapping("creationClient/{numCheque}")
	public ResponseEntity<Client> CreationClient(@PathVariable("numCheque") Long numCheque, @RequestBody Client client) {
		Long id = client.getId();
	    if (repoclient.existsById(id)|| client.getId() == null) {
	        List<Cheque> liste = client.getCheques();
	        for (int i = 0; i < liste.size(); i++) {
	            if (liste.get(i).getNumCheque().equals(numCheque)) {
	                return new ResponseEntity<>(client, HttpStatus.OK);
	            }
	        }
	        Cheque ch = chequeBorSer.RechercherCheque(numCheque);
	        liste.add(ch);
	        client.setCheques(liste);
	        repoclient.save(client);
	        ch.setClientCh(client);
	        chequeRepo.save(ch);
	        return new ResponseEntity<>(client, HttpStatus.OK);

	    }
	    Cheque ch = chequeBorSer.RechercherCheque(numCheque);
	    ch.setClientCh(client); 
	    List<Cheque> listeCh = new ArrayList<>();
	    listeCh.add(ch);
	    client.setCheques(listeCh);
	    repoclient.save(client);
	    chequeRepo.save(ch); 
	    return new ResponseEntity<>(client, HttpStatus.OK);
	}

	
	@GetMapping("/creeCompte/{numCompte}")
	public ResponseEntity<Compte> creeCompte(@PathVariable("numCompte") Long numCompte) {
	        Compte c = chequeBorSer.trouverCompte(numCompte);
	        if (c == null) {
	            Compte compte = new Compte(numCompte, null, null);
	            return new ResponseEntity<>(compte, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(c, HttpStatus.OK);
	        }
	   
	}

	
	
	@PutMapping("creationCompte/{numCheque}")
	 public ResponseEntity<Compte> CreationCompte(@PathVariable("numCheque")Long numCheque,@RequestBody Compte compte) {
		Cheque ch=chequeBorSer.RechercherCheque(numCheque);
		Client c=compte.getClient();
		if(c==null) {
			Client client=ch.getClientCh();
			compte.setClient(client);
		    repocompte.save(compte);
		    client.setCompte(compte);
		    repoclient.save(client);
		    return new ResponseEntity<>(compte, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(compte, HttpStatus.OK);
		}
		
		
	}
	
	
	@GetMapping("/creeBanqueTunisie/{codeBanque}/{nomBanque}/{codeSwift}/{nomTireur}")
	 public ResponseEntity<Banque_destinataire> creeBnaqueT(@PathVariable("codeBanque")Long codeBanque,@PathVariable("nomBanque")String nomBanque,@PathVariable("codeSwift")String codeSwift,@PathVariable("nomTireur")String nomTireur) {
		 Banque_destinataire  banque = new Banque_destinataire(codeBanque,nomBanque,codeSwift,null,null,null,null,nomTireur,null);
		return new ResponseEntity<>(banque,HttpStatus.OK);
	}
	
	@GetMapping("/creeBanqueCredit/{codeBanque}/{codePays}/{codeSwift}/{nomTireur}")
	 public ResponseEntity<Banque_destinataire> creeCompte(@PathVariable("codeBanque")Long codeBanque,@PathVariable("codePays")Long codePays,@PathVariable("codeSwift")String codeSwift,@PathVariable("nomTireur")String nomTireur) {
		Banque_destinataire  banque = new Banque_destinataire(codeBanque,null,codeSwift,null,null,codePays,null,nomTireur,null);

		return new ResponseEntity<>(banque,HttpStatus.OK);
	}
	
	
	@PutMapping("creationBanque/{numCheque}")
	 public ResponseEntity<Banque_destinataire> creationBanqueE(@PathVariable("numCheque")Long numCheque,@RequestBody Banque_destinataire banque) {
		Cheque ch=chequeBorSer.RechercherCheque(numCheque);
		List<Cheque>liste=banque.getCheque();
		if(liste==null) {
			List<Cheque>listeCh=new ArrayList<>();
			listeCh.add(ch);
			banque.setCheque(listeCh);
		}
		else {
			liste.add(ch);
			banque.setCheque(liste);
		}
		
		StatutCheque statut=null;
		
		if(banque.getSwiftBanque().equals("MT400")) {
			ch.setStatut(statut.Payé);
			
		}else if(banque.getSwiftBanque().equals("MT410")){
			ch.setStatut(statut.En_cours_de_traitement);
			
		}else if(banque.getSwiftBanque().equals("MT456")) {
			ch.setStatut(statut.Impayé);
			
		}else {
			ch.setStatut(statut.Envoyé);
			
		}
		ch.setDateSortie(new Date());
		repobanque.save(banque);
		ch.setBanque(banque);
		chequeRepo.save(ch);
		return new ResponseEntity<>(banque,HttpStatus.OK);
	}
	
	
	//remise 3
	
	@GetMapping("/creeBanqueCouverture/{codeBanque}/{nomBanque}/{codeSwift}")
	 public ResponseEntity<Banque_couverture> creeBaNqueC(@PathVariable("codeBanque")Long codeBanque,@PathVariable("nomBanque")String nomBanque,@PathVariable("codeSwift")String codeSwift) {
		Banque_couverture banqueC = new Banque_couverture(codeBanque,nomBanque,codeSwift, null, null,null,null);
		return new ResponseEntity<>(banqueC,HttpStatus.OK);
	}
	
	@PutMapping("creationBanqueCouverture/{numCheque}")
	 public ResponseEntity<Banque_couverture> creationBanqueC(@PathVariable("numCheque")Long numCheque,@RequestBody Banque_couverture banque) {
		Cheque ch=chequeBorSer.RechercherCheque(numCheque);
		List<Cheque>liste=banque.getCheque();
		if(liste==null) {
			List<Cheque>listeCh=new ArrayList<>();
			listeCh.add(ch);
			banque.setCheque(listeCh);
		}
		else {
			liste.add(ch);
			banque.setCheque(liste);
		}
		ch.setStatut(StatutCheque.Envoyé);
		ch.setDateSortie(new Date());
		repoBanqueC.save(banque);
		ch.setBanque(banque);
		chequeRepo.save(ch);
		return new ResponseEntity<>(banque,HttpStatus.OK);
	}
	 
	    @GetMapping("/creeBanqueDestinataire/{codeBanque}/{nomBanque}/{codeSwift}/{nomTireur}/{adresse}")
	    public ResponseEntity<Banque_destinataire> creeBaNqueC(@PathVariable("codeBanque")Long codeBanque,@PathVariable("nomBanque")String nomBanque,
			 @PathVariable("codeSwift")String codeSwift,@PathVariable("nomTireur")String nomTireur,@PathVariable("adresse")String adresse) {
	    	
		Banque_destinataire  banqueD = new Banque_destinataire(codeBanque,nomBanque,codeSwift,null,null,null,null,nomTireur,adresse);
		return new ResponseEntity<>(banqueD,HttpStatus.OK);
	}
	 
		@PutMapping("creationBanqueDestinataire/{numCheque}")
		 public ResponseEntity<Banque_destinataire> creationBanqueC(@PathVariable("numCheque")Long numCheque,@RequestBody Banque_destinataire banque) {
			Cheque ch=chequeBorSer.RechercherCheque(numCheque);
			List<Cheque>liste=banque.getCheque();
			if(liste==null) {
				List<Cheque>listeCh=new ArrayList<>();
				listeCh.add(ch);
				banque.setCheque(listeCh);
			}
			else {
				liste.add(ch);
				banque.setCheque(liste);
			}
			ch.setStatut(StatutCheque.Envoyé);
			ch.setDateSortie(new Date());
			repoBanqueD.save(banque);
			ch.setBanque(banque);
			chequeRepo.save(ch);
			return new ResponseEntity<>(banque,HttpStatus.OK);
		}
	
	
	
	
	
	
	//Suivie Agence
	//suivie recu
	@GetMapping("/afficherListeRecu")
	 public ResponseEntity<List<Cheque>> ListeDesChequesRecu() {
		List<Cheque> liste =chequeBorSer.listeRecu();
		return new ResponseEntity<>(liste, HttpStatus.OK);
	}
	
	@PutMapping("/updateRecu")
	 public ResponseEntity<Cheque> updateRecu(@RequestBody Cheque ch) {
		StatutCheque statut=null;
		ch.setStatut(statut.Reçu);
		ch.setDateReception(new Date());
		chequeRepo.save(ch);
		return new ResponseEntity<>(ch, HttpStatus.OK);
	}
	
	@PutMapping("/updateRejet/{motif}")
	 public ResponseEntity<Cheque> updateRejet(@PathVariable("motif")String motif,@RequestBody Cheque ch) {
		StatutCheque statut=null;
		ch.setStatut(statut.rejeté);
		ch.setDateRejet(new Date());
		ch.setMotifRejet(motif);
		chequeBorSer.mailRejetAgence(ch);
		chequeBorSer.mailRejetClient(ch);
		chequeRepo.save(ch);
		return new ResponseEntity<>(ch, HttpStatus.OK);
	}
	
	@PutMapping("/updateRejetRecu")
	 public ResponseEntity<Cheque> updateRejetRecu(@RequestBody Cheque ch) {
		StatutCheque statut=null;
		ch.setStatut(statut.rejetéReçu);
		ch.setDateReceptionRejet(new Date());
		chequeRepo.save(ch);
		return new ResponseEntity<>(ch, HttpStatus.OK);
	}
	
	//suivie rejete
	@GetMapping("/afficherListeRejete")
	 public ResponseEntity<List<Cheque>> ListeRejete() {
		List<Cheque> liste =chequeBorSer.listeRejete();
		return new ResponseEntity<>(liste, HttpStatus.OK);
	}
	
	//suivie traite
	@GetMapping("/afficherListeTraite")
	 public ResponseEntity<List<Cheque>> ListeDesChequesTraites() {
		List<Cheque> liste =chequeBorSer.afficherChequesTraite();
		return new ResponseEntity<>(liste, HttpStatus.OK);
	}
	
	
	//admin
	//affichage cheques
	
	@GetMapping("/affichertoutcheques")
	 public ResponseEntity<List<Cheque>> Listetoutcheques() {
		List<Cheque> liste =chequeBorSer.afficherToutCheques();
		return new ResponseEntity<>(liste, HttpStatus.OK);
	}
	
	//suppression cheque
	
	@DeleteMapping("/supprimer")
    public ResponseEntity<?> supprimerCheque(@RequestBody Cheque cheque) {
        chequeBorSer.supprimerCheque(cheque);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	//modifier cheque
	@GetMapping("/cheque/{numCheque}/{devise}/{montant}")
	 public ResponseEntity<Cheque> nouveaucheque(@PathVariable("numCheque")Long numCheque,@PathVariable("devise")String devise,@PathVariable("montant")Float montant) {
		if(chequeBorSer.LonguerCheque(numCheque)==1) {
			Cheque ch=new Cheque(numCheque,montant,devise,null,null,null,null,null,null,null,null,null,null);
			return new ResponseEntity<>(ch, HttpStatus.OK);
		}
		else {
			return null;
		}
		
	}
	
	@PutMapping("/modifierCheque/{numCheque}")
	 public ResponseEntity<Cheque> modifierCheque(@PathVariable("numCheque")Long numCheque,@RequestBody Cheque cheque) {
		Cheque ch=chequeBorSer.RechercherCheque(numCheque);
		ch.setNumCheque(cheque.getNumCheque());
		ch.setDevise(cheque.getDevise());
		ch.setMontant(cheque.getMontant());
		chequeRepo.save(ch);
		return new ResponseEntity<>(ch, HttpStatus.OK);
	}
	//client admin
	//affichage clients
	@GetMapping("/afficherClients")
	 public ResponseEntity<List<Client>> ListeClient() {
		List<Client> liste =chequeBorSer.afficherclients();
		return new ResponseEntity<>(liste, HttpStatus.OK);
	}
	
	//suppression client
	
	@DeleteMapping("/supprimerClient")
	public ResponseEntity<?> supprimerClient(@RequestBody Client client) {
	chequeBorSer.supprimerClient(client);
	return new ResponseEntity<>(HttpStatus.OK);
	    }
	
	//modifier client
		@GetMapping("/client/{nomClient}/{prenomClient}/{numeroTelephone}/{mailClient}")
		 public ResponseEntity<Client> nouveauclient(@PathVariable("nomClient")String nomClient,@PathVariable("prenomClient")String prenomClient,
				 @PathVariable("numeroTelephone")Long numeroTelephone,@PathVariable("mailClient")String mailClient ) {
			Client client = new Client();
			client.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
			client.setNomClient(nomClient);
			client.setPrenomClient(prenomClient);
			client.setNumeroTelephone(numeroTelephone); //mail men8ir @
			client.setMailClient(mailClient);
			return new ResponseEntity<>(client,HttpStatus.OK);
		}
		
		@PutMapping("/modifierClient/{id}")
		 public ResponseEntity<Client> modifierCheque(@PathVariable("id")Long id,@RequestBody Client clientN) {
			Client client=chequeBorSer.RechercherClient(id);
			client.setNomClient(clientN.getNomClient());
			client.setPrenomClient(clientN.getPrenomClient());
			client.setNumeroTelephone(clientN.getNumeroTelephone());
			client.setMailClient(clientN.getMailClient());
			repoclient.save(client);
			return new ResponseEntity<>(client, HttpStatus.OK);
		}
		
		//agence admin
		//affichage agence
		@GetMapping("/afficherAgence")
		 public ResponseEntity<List<Agence>> ListeAgence() {
			List<Agence> liste =chequeBorSer.afficherAgences();
			return new ResponseEntity<>(liste, HttpStatus.OK);
		}
		
		//suppression agence
		
		@DeleteMapping("/supprimerAgence")
		public ResponseEntity<?> supprimerAgence(@RequestBody Agence agence) {
		chequeBorSer.supprimerAgence(agence);
		return new ResponseEntity<>(HttpStatus.OK);
		}
		
		//modifier agence
		@GetMapping("/agence/{nomAgence}/{adresse}/{telephone}")
		public ResponseEntity<Agence> nouveauAgence(@PathVariable("nomAgence")String nomAgence,
				@PathVariable("adresse")String adresse,@PathVariable("telephone")Long telephone) {
		Agence agence =new Agence();
		agence.setCodeAgence(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
		agence.setAdresse(adresse);
		agence.setNomAgence(nomAgence);
		agence.setTelephone(telephone);
		return new ResponseEntity<>(agence,HttpStatus.OK);
				}
				
	    @PutMapping("/modifierAgence/{codeAgence}")
		public ResponseEntity<Agence> modifierAgence(@PathVariable("codeAgence")Long codeAgence,@RequestBody Agence agenceN) {
		Agence agence=chequeBorSer.RechercherAgence(codeAgence);
		agence.setNomAgence(agenceN.getNomAgence());
		agence.setAdresse(agenceN.getAdresse());
		agence.setTelephone(agenceN.getTelephone());
		agenceRepo.save(agence);
		return new ResponseEntity<>(agence, HttpStatus.OK);
		}
	    
	  	
	    //ajouter agence
	    
	    @GetMapping("/agenceobjet/{codeAgence}/{nomAgence}/{adresse}/{telephone}")
		public ResponseEntity<Agence> objetAgence(@PathVariable("codeAgence")Long codeAgence,@PathVariable("nomAgence")String nomAgence,
		@PathVariable("adresse")String adresse,@PathVariable("telephone")Long telephone) {
		Agence agence =new Agence();
		agence.setCodeAgence(codeAgence);
		agence.setAdresse(adresse);
		agence.setNomAgence(nomAgence);
		agence.setTelephone(telephone);
		return new ResponseEntity<>(agence,HttpStatus.OK);
				}
				
	    
	  	@PostMapping("/AjouterAgence/{codeAgence}")
	  	public ResponseEntity<Agence> AjouterAgence(@PathVariable("codeAgence")Long codeAgence,@RequestBody Agence agenceN) {
	  	Agence agence=chequeBorSer.RechercherAgence(codeAgence);
	  	if(agence!=null) {
	  		agence=null;
	  		return new ResponseEntity<>(agence, HttpStatus.OK);
	  	}else {
	  	agenceRepo.save(agenceN);
	  	return new ResponseEntity<>(agenceN, HttpStatus.OK);
	  	}
	  	}
	    
	    
	    //el alwen	
	    @GetMapping("/nbrjour")
		public ResponseEntity<List<Cheque>> nbrjour() {
	    	List<Cheque> liste=chequeBorSer.afficherChequeAvecCouleur();
			return new ResponseEntity<>(liste,HttpStatus.OK);
		}
		
		//dashboard
	    
	    @GetMapping("/nbrcheques")
		public ResponseEntity<Integer> nbrcheques() {
	    	Integer a=chequeBorSer.afficherToutCheques().size();
			return new ResponseEntity<>(a, HttpStatus.OK);
		}
	    
	    @GetMapping("/nbrbordereau")
		public ResponseEntity<Integer> nbrbordereau() {
	    	Integer a=chequeBorSer.afficherBordereaux().size();
			return new ResponseEntity<>(a, HttpStatus.OK);
		}
	    
	    @GetMapping("/nbrclient")
		public ResponseEntity<Integer> nbrclient() {
	    	Integer a=chequeBorSer.afficherclients().size();
			return new ResponseEntity<>(a, HttpStatus.OK);
		}
	    
	    @GetMapping("/nbragence")
	  		public ResponseEntity<Integer> nbragence() {
	  	    	Integer a=chequeBorSer.afficherAgences().size();
	  			return new ResponseEntity<>(a, HttpStatus.OK);
	  	}
	    
	 
	
	
	//to test list
	@GetMapping("/nombre1")
	public ResponseEntity<Integer> nombre(@RequestParam("numbordereau")Long numBordereau) {
		Bordereau bordereau=chequeBorSer.RechercherBordereau(numBordereau);
		Integer a=bordereau.getListeCh().size();
		return new ResponseEntity<>(a, HttpStatus.OK);
	}

}
