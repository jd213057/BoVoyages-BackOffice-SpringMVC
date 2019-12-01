package fr.gtm.backoffice.rest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.gtm.backoffice.entities.Commercial;
import fr.gtm.backoffice.entities.DatesVoyage;
import fr.gtm.backoffice.entities.Destination;
import fr.gtm.backoffice.repositories.CommercialRepository;
import fr.gtm.backoffice.repositories.DatesVoyageRepository;
import fr.gtm.backoffice.repositories.DestinationRepository;


/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0 Controller de type REST de BoVoyages-BackOffice.
 * Controller de type Rest du Back Office BoVoyages.
 */
@RestController
public class BackOfficeRestController {

	@Autowired
	private CommercialRepository commercialRepo;
	@Autowired
	private DestinationRepository destinationRepo;
	@Autowired
	private DatesVoyageRepository datesVoyageRepo;

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
	@GetMapping("/destination/dates/valid/{id}")
	public List<DatesVoyage> getValidDatesVoyagesByDestinationId(@PathVariable("id") long id) {
		List<DatesVoyage> datesVoyages = new ArrayList<>();
		Destination destination = destinationRepo.getDestinationWithDatesById(id);
		if (destination.isRaye())
			return null;
		for (DatesVoyage d : destination.getDatesVoyages()) {
			if (!d.isDeleted())
				datesVoyages.add(d);
		}
		return datesVoyages;
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
	@GetMapping("/destination/byRegion")
	public List<Destination> getDestinationsByRegion(@RequestParam(name = "region") String region) {
		List<Destination> destinations = destinationRepo.findDestinationByRegion(region);
		List<Destination> destinationsFinal = new ArrayList<>();
		for (Destination d : destinations) {
			if (!d.isRaye())
				destinationsFinal.add(d);
		}
		return destinationsFinal;
	}

	/**
	 * @return la liste de toutes les DestinationDTO valides (non rayees).
	 */
	@GetMapping("/destination/valid")
	public List<Destination> getDestinationNotDeleted() {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		return destinations;
	}

	/**
	 * @param id de type long.
	 */
	@PostMapping("/destination/delete/{id}")
	public void deleteDestinationById(@PathVariable(name = "id") long id) {
		Destination destination = destinationRepo.findById(id).get();
		destination.setRaye(true);
		destinationRepo.save(destination);
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
	public boolean connexionTo(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password)
			throws NoSuchAlgorithmException {
		boolean isAuth;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hash = md.digest(password.getBytes());
		BigInteger number = new BigInteger(1, hash);
		StringBuilder hexString = new StringBuilder(number.toString(16));
		while (hexString.length() < 64) {
			hexString.insert(0, '0');
		}
		String pwdFin = hexString.toString();

		Optional<Commercial> opt = commercialRepo.findByNomAndHashPassword(username, pwdFin);

		if (opt.isPresent()) {
			isAuth = true;
			return isAuth;
		}
		isAuth = false;
		return isAuth;
	}
	
	/**
	 * @param nom de type String.
	 * @param password de type String.
	 * @return isAuth de type boolean.
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/signup")
	public boolean createCommercial(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password)
			throws NoSuchAlgorithmException {
		boolean isAuth;
		Optional<Commercial> client = commercialRepo.findByUsername(username);
		if (!client.isPresent()) {
			commercialRepo.createNewCommercial(username, password);
			isAuth=true;
			return isAuth;
		}
		 isAuth=false;
		return isAuth;

	}
	
	
	
	
	
}
