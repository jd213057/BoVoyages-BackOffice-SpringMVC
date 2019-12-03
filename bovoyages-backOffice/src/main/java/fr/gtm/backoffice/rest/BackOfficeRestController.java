package fr.gtm.backoffice.rest;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.gtm.backoffice.entities.Commercial;
import fr.gtm.backoffice.entities.DatesVoyage;
import fr.gtm.backoffice.entities.Destination;
import fr.gtm.backoffice.images.StorageFileNotFoundException;
import fr.gtm.backoffice.images.StorageService;
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
	
	private final StorageService storageService;

	@Autowired
	public BackOfficeRestController(StorageService storageService) {
		this.storageService = storageService;
	}
	
	
	
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
	@GetMapping("/home")
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
	
	@PostMapping("updatedestination2")
	public String updateDestination2(@RequestParam(name="id") long id, @RequestParam(name="region") String region, @RequestParam(name="description") String description,Model model) {
    Destination destination = destinationRepo.findById(id).get();
	destination.setDescription(description);
	destination.setRegion(region);
	destinationRepo.save(destination);
	List<Destination> destinations = destinationRepo.getValidDestinations();
	model.addAttribute("destinations",destinations);
	return "destinations";
	}
	
	
	@PostMapping("updatedestination")
	public String updateDestination(@RequestParam(name="id") long id, Model model) {
		Destination destination = destinationRepo.findById(id).get();
		model.addAttribute("destination",destination);
	return "updateDestinationForm";
	}
	
	
	
	@PostMapping("createdestination")
	public String createDestination(@RequestParam(name="region") String region, @RequestParam(name="description") String description,Model model) {
	List<Destination> destinations =  destinationRepo.findDestinationByRegion(region);
	Boolean error  = false;
	for(Destination d : destinations) {
		if(d.getRegion().equals(region) && !d.isRaye()) {
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
	
	@PostMapping("createdatesvoyage")
	public String createDatesVoyage(@RequestParam(name="idDestination") long idDestination, @RequestParam(name = "dateAller") String dateAller,
			@RequestParam(name = "dateRetour") String dateRetour, @RequestParam(name = "nbrePlaces") int nbrePlaces,
			@RequestParam(name = "prixHT") float prixHT, Model model) throws ParseException {
	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date dateAllerF= fmt.parse(dateAller);
		Date dateRetourF=fmt.parse(dateRetour);
		DatesVoyage dateVoyage  = new DatesVoyage(dateAllerF, dateRetourF, prixHT, nbrePlaces);
	destinationRepo.findById(idDestination).get().getDatesVoyages().add(dateVoyage);
	destinationRepo.save(destinationRepo.findById(idDestination).get());
	datesVoyageRepo.save(dateVoyage);
//	List<Destination> destinations = destinationRepo.getValidDestinations();
	List<DatesVoyage> datesVoyages = destinationRepo.findById(idDestination).get().getDatesVoyages();
	for (DatesVoyage d : datesVoyages) {
		if(d.isDeleted()) datesVoyages.remove(d);};
	model.addAttribute("destination",destinationRepo.findById(idDestination).get());
	model.addAttribute("datesVoyages",datesVoyages);
	return "destinationdetails";	
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
	 * @param DatesVoyageId de type long.
	 * @return une destination de type Destination correspondant à la date de voyage selectionnee.
	 */
	@GetMapping("/dates/destination/{id}")
	public Destination getDestinationByDatesVoyageId(@PathVariable("id")long DatesVoyageId) {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		for (Destination d : destinations) {
	     List<DatesVoyage> datesvoyages = getValidDatesVoyagesByDestinationId(d.getId());
		for( DatesVoyage dv : datesvoyages) {
			if(dv.getId()==(DatesVoyageId)) {
				return d;
			}
		}	
	}
		return null;}
	
	/**
	 * @param id de type long.
	 * @return une liste de DatesVoyage de toutes les dates valides pour une destination voulue.
	 */
	@GetMapping("/destination/dates/valid/{id}")
	public List<DatesVoyage> getValidDatesVoyagesByDestinationId(@PathVariable("id") long id) {
		List<DatesVoyage> datesVoyages = new ArrayList<>();
		Destination destination = destinationRepo.getDestinationWithDatesById(id);
		if(destination.isRaye())return null;
		for (DatesVoyage d : destination.getDatesVoyages()) {
			if (!d.isDeleted())
				datesVoyages.add(d);
		}
		return datesVoyages;
	}
	/**
	 * @param id de type long.
	 */
	@PostMapping("/deletedatevoyage")
	public String deleteDatesVoyageById(@RequestParam(name = "id") long id, Model model) {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		DatesVoyage datesVoyage = datesVoyageRepo.findById(id).get();
		datesVoyage.setDeleted(true);
		datesVoyageRepo.save(datesVoyage);
		model.addAttribute("destinations",destinations);
		return "destinations";
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
	
	@PostMapping("/images")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(BackOfficeRestController.class,
						"serveFile", path.getFileName().toString()).build().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/images2")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "home";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("backtodestinations")
	public String getBacktoDestinationsPage(Model model) {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		model.addAttribute("destinations", destinations);
		return "destinations";
	}

}

	
	
	
	
	

