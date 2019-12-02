package fr.gtm.backoffice.rest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.gtm.backoffice.entities.Commercial;
import fr.gtm.backoffice.entities.DatesVoyage;
import fr.gtm.backoffice.entities.Destination;
import fr.gtm.backoffice.repositories.CommercialRepository;
import fr.gtm.backoffice.repositories.DatesVoyageRepository;
import fr.gtm.backoffice.repositories.DestinationRepository;
import fr.gtm.backoffice.util.Digest;


/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0 Controller de type REST de BoVoyages-BackOffice.
 * Controller de type Rest du Back Office BoVoyages.
 */
@Controller
@RequestMapping("/")
public class BackOfficeRestController {

	/**
	 * Attribut commercialRepo de type CommercialRepository.
	 */
	@Autowired
	private CommercialRepository commercialRepo;
	/**
	 * Attribut destinationRepo de type DestinationRepository.
	 */
	@Autowired
	private DestinationRepository destinationRepo;
	/**
	 * Attribut datesVoyageRepo de type DatesVoyageRepository.
	 */
	@Autowired
	private DatesVoyageRepository datesVoyageRepo;
	
	
	/**
	 * @param model de type Model.
	 * @return la page HTML "index";
	 */
	@GetMapping(path="/")
	public String signin( Model model) {
		Commercial commercial = new Commercial();
		model.addAttribute("commercial",commercial);
		return "index";
	}
	
	/**
	 * @return la page HTML "home".
	 */
	@PostMapping("/home")
	public String home() {
		return "home";
	}
	
	/**
	 * @param destination de type Destination
	 * @return une confirmation de type String de la création d'une nouvelle
	 *         destination.
	 */
	@PostMapping("/destination/new")
	public String createDestination(@RequestBody Destination destination) {
		destination.setRaye(false);
		destinationRepo.save(destination);
		return "Votre destination " + destination.getRegion() + " avec comme description : "
				+ destination.getDescription() + " a bien été sauvegardé dans la base de données.";
	}

	/**
	 * @param id de type long.
	 * @return une liste de DatesVoyage de toutes les dates valides pour une
	 *         destination voulue.
	 */
	@PostMapping("destinationinfo")
	public String getValidDatesVoyagesByDestinationId(@RequestParam("id") long id, Model model) {
		List<DatesVoyage> datesVoyages = new ArrayList<>();
		Destination destination = destinationRepo.getDestinationWithDatesById(id);
		if (destination.isRaye())
			return null;
		for (DatesVoyage d : destination.getDatesVoyages()) {
			if (!d.isDeleted())
				datesVoyages.add(d);
		}
		model.addAttribute("destination", destination);
		model.addAttribute("datesVoyages",datesVoyages);
		return "destinationdetails";
	}

	/**
	 * @param id de type long
	 * @return une destination de type destination correspondant à l'id en argument
	 *         de la méthode.
	 */
	@GetMapping("/destination/{id}")
	public Destination getDestinationById(@PathVariable("id") long id) {
		Destination destination = destinationRepo.findById(id).get();
		if (destination.isRaye())
			return null;
		return destination;
	}

	/**
	 * @param region de type String
	 * @return la liste de toutes les destinations correspondante à la recherche par
	 *         region associee.
	 */
	@PostMapping("/destinationByRegion")
	public String getDestinationsByRegion(@RequestParam(name = "region") String region, Model model) {
		List<Destination> destinations = destinationRepo.findDestinationByRegion(region);
		List<Destination> destinationsFinal = new ArrayList<>();
		for (Destination d : destinations) {
			if (!d.isRaye())
				destinationsFinal.add(d);
		}
		model.addAttribute("destinations",destinationsFinal);
		return "destinations";
	}

	/**
	 * @return la liste de toutes les DestinationDTO valides (non rayees).
	 */
	@PostMapping("/destinationvalid")
	public String getDestinationNotDeleted(Model model) {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		model.addAttribute("destinations",destinations);
		return "destinations";
	}
	
	@PostMapping("createdestination")
	public String createDestination(@RequestParam(name="region") String region, @RequestParam(name="description") String description,Model model) {
	List<Destination> destinations =  destinationRepo.findDestinationByRegion(region);
	Boolean error  = false;
	for(Destination d : destinations) {
		if(d.getRegion().equals(region)&& !d.isRaye()) {
			error = true;
			model.addAttribute("error",error);
			List<Destination> destinations2 = destinationRepo.getValidDestinations();
			model.addAttribute("destinations",destinations2);
			return "destinations";
			
		}
	}
	destinationRepo.save(new Destination(region, description, false));
	return "home";
	}
	/**
	 * @param id de type long.
	 */
	@PostMapping("deletedestination")
	public String deleteDestinationById(@RequestParam(name = "id") long id, Model model) {
		Destination destination = destinationRepo.findById(id).get();
		destination.setRaye(true);
		destinationRepo.save(destination);
		return "home";
	}

	/**
	 * @param datesVoyage de type DatesVoyage
	 * @return une confirmation de type String suite à la creation d'une nouvelle date de voyage.
	 */
	@PostMapping("/destination/date/new")
	public String createDatesVoyage(@RequestBody DatesVoyage datesVoyage) {
		datesVoyage.setDeleted(false);
		datesVoyageRepo.save(datesVoyage);
		return "Votre date de voyage avec comme description : " + datesVoyage.toString()
				+ " a bien été sauvegardé dans la base de données.";
	}
	/**
	 * @param id de type long.
	 */
	@PostMapping("/destination/date/delete/{id}")
	public void deleteDatesVoyageById(@PathVariable(name = "id") long id) {
		DatesVoyage datesVoyage = datesVoyageRepo.findById(id).get();
		datesVoyage.setDeleted(true);
		datesVoyageRepo.save(datesVoyage);
	}
	
	/**
	 * @param nom de type String.
	 * @param password de type String.
	 * @return is Auth de type boolean.
	 * @throws NoSuchAlgorithmException.
	 */
	@PostMapping("/connexion")
	public String connexionTo(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password)
			throws NoSuchAlgorithmException {
		boolean isAuth;
//		MessageDigest md = MessageDigest.getInstance("SHA-256");
//		byte[] hash = md.digest(password.getBytes());
//		BigInteger number = new BigInteger(1, hash);
//		StringBuilder hexString = new StringBuilder(number.toString(16));
//		while (hexString.length() < 64) {
//			hexString.insert(0, '0');
//		}
		String pwdFin = Digest.getStringSha256(password);;

		Optional<Commercial> opt = commercialRepo.findByNomAndHashPassword(username, pwdFin);
		
		isAuth= opt.isPresent()? true : false;
//		if (opt.isPresent()) {
//			isAuth = true;
//			return isAuth;
//		}
//		isAuth = false;
//		return isAuth;
		if(isAuth) return "home";
		return "index";
	}
	
	@PostMapping("/signupform")
	public String signing(Model model) {
		Commercial commercial = new Commercial();
		model.addAttribute("commercial",commercial);
		return "signup";
	}
	
	/**
	 * @param nom de type String.
	 * @param password de type String.
	 * @return isAuth de type boolean.
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/confirmsignup")
	public String createCommercial(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, Model model)
			throws NoSuchAlgorithmException {
		Optional<Commercial> commercial = commercialRepo.findByUsername(username);
		boolean isAuth = !commercial.isPresent()? true : false;
		if (!commercial.isPresent()) {
			commercialRepo.createNewCommercial(username, password);
			Optional<Commercial> commercial2 = commercialRepo.findByUsername(username);
			
		}
		return !commercial.isPresent()? "home": "signup";	
	}
	
	
	
	
	
	
	
}
