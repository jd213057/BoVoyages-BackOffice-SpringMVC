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
import org.springframework.web.bind.annotation.RequestMethod;
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
 * @version 1.0 Controller de type REST de BoVoyages-BackOffice. Controller de
 *          type Rest du Back Office BoVoyages.
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
	 * attribut storageService de type StorageService.
	 */
	private final StorageService storageService;

	/**
	 * @param storageService de type StorageService.
	 */
	@Autowired
	public BackOfficeRestController(StorageService storageService) {
		this.storageService = storageService;
	}

	/**
	 * @param model de type Model.
	 * @return la page HTML "index";
	 */
	@GetMapping(path = "/")
	public String signin(Model model) {
		Commercial commercial = new Commercial();
		model.addAttribute("commercial", commercial);
		return "index";
	}

	/**
	 * @return la page HTML "home".
	 */
	@GetMapping("/home")
	public String home() {
		return "home";
	}
	
	
	@GetMapping("/index")
	public String index1() {
		return "index";
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
		Destination destination = destinationRepo.findById(id).get();
		if (destination.isRaye())
			return null;
		for (DatesVoyage d : destination.getDatesVoyages()) {
			if (!d.isDeleted())
				datesVoyages.add(d);
		}
		model.addAttribute("destination", destination);
		model.addAttribute("datesVoyages", datesVoyages);
		model.addAttribute("destinations", destinationRepo.getValidDestinations());
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
		model.addAttribute("destinations", destinationsFinal);
		return "destinations";
	}

	/**
	 * @return la liste de toutes les DestinationDTO valides (non rayees).
	 */
	@PostMapping("/destinationvalid")
	public String getDestinationNotDeleted(Model model) {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		model.addAttribute("destinations", destinations);
		return "destinations";
	}
	
	@GetMapping("/destinationvalid")
	public String getDestinationNotDeleted1(Model model) {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		model.addAttribute("destinations", destinations);
		return "destinations";
	}

	/**
	 * @param id          de type long.
	 * @param region      de type String.
	 * @param description de type String.
	 * @param model       de type Model.
	 * @return la page HTMl destinations.
	 */
	@PostMapping("updatedestination2")
	public String updateDestination2(@RequestParam(name = "id") long id, @RequestParam(name = "region") String region,
			@RequestParam(name = "description") String description, Model model) {
		Destination destination = destinationRepo.findById(id).get();
		destination.setDescription(description);
		destination.setRegion(region);
		destinationRepo.save(destination);
		List<Destination> destinations = destinationRepo.getValidDestinations();
		model.addAttribute("destinations", destinations);
		return "destinations";
	}

	/**
	 * @param id    de type long.
	 * @param model de type Model.
	 * @return le formulaire de mofidication d'une destination de type page HTML.
	 */
	@PostMapping("updatedestination")
	public String updateDestination(@RequestParam(name = "id") long id, Model model) {
		Destination destination = destinationRepo.findById(id).get();
		model.addAttribute("destination", destination);
		return "updateDestinationForm";
	}

	/**
	 * @param region      de type String.
	 * @param description de type String.
	 * @param model       de type Model.
	 * @return la page HTML destinations.
	 */
	@PostMapping("createdestination")
	public String createDestination(@RequestParam(name = "region") String region,
			@RequestParam(name = "description") String description, Model model) {
		List<Destination> destinations = destinationRepo.findDestinationByRegion(region);
		Boolean error = false;
		for (Destination d : destinations) {
			if (d.getRegion().equals(region) && !d.isRaye()) {
				error = true;
				model.addAttribute("error", error);
				List<Destination> destinations2 = destinationRepo.getValidDestinations();
				model.addAttribute("destinations", destinations2);
				return "destinations";

			}
		}
		destinationRepo.save(new Destination(region, description, false));
		List<Destination> destinations2 = destinationRepo.getValidDestinations();
		model.addAttribute("destinations", destinations2);
		return "destinations";
	}

	/**
	 * @param idDestination de type long.
	 * @param dateAller     de type Date.
	 * @param dateRetour    de type Date.
	 * @param prixHT        de type float.
	 * @param nbrePlaces    de type Int.
	 * @param model         de type Model.
	 * @return la page HTML destinationdetails.
	 * @throws ParseException
	 */
	@PostMapping("createdatesvoyage")
	public String addDatesVoyagesToDestination(@RequestParam("idDestination") long idDestination,
			@RequestParam("dateAller") String dateAller, @RequestParam("dateRetour") String dateRetour,
			@RequestParam("prixHT") float prixHT, @RequestParam("nbrePlaces") int nbrePlaces, Model model)
			throws ParseException {
		SimpleDateFormat franck = new SimpleDateFormat("yyyy-MM-dd");
		Destination dest = destinationRepo.findById(idDestination).get();
		List<DatesVoyage> listeDates = dest.getDatesVoyages();
		Date newAller = franck.parse(dateAller);
		Date newRetour = franck.parse(dateRetour);
		DatesVoyage newDate = new DatesVoyage();
		newDate.setDateAller(newAller);
		newDate.setDateRetour(newRetour);
		newDate.setDeleted(false);
		newDate.setNbrePlaces(nbrePlaces);
		newDate.setPrixHT(prixHT);
		listeDates.add(newDate);
		dest.setDatesVoyages(listeDates);
		destinationRepo.save(dest);
		Destination destination = destinationRepo.findById(idDestination).get();
		model.addAttribute("destination", destination);
		List<DatesVoyage> datess = destination.getDatesVoyages();
		List<DatesVoyage> datesVoyages = new ArrayList<DatesVoyage>();
		for (DatesVoyage d : datess) {
			if (d.isDeleted() == false) {
				datesVoyages.add(d);
			}
		}
		model.addAttribute("datesVoyages", datesVoyages);
		return "destinationdetails";
	}

	/**
	 * @param idDate de type long.
	 * @param model  de type Model.
	 * @return le formulaire de modification des dates de type page HTML.
	 * @throws ParseException
	 */
	@PostMapping("modifierdate")
	public String modifyDatesVoyage(@RequestParam(name = "idDate") long idDate, Model model) throws ParseException {
		DatesVoyage dateVoyage = datesVoyageRepo.findById(idDate).get();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String dateAllerF = fmt.format(dateVoyage.getDateAller());
		Date dateAllerF2 = fmt.parse(dateAllerF);
		dateVoyage.setDateAller(dateAllerF2);
		String dateRetourF = fmt.format(dateVoyage.getDateRetour());
		Date dateRetourF2 = fmt.parse(dateRetourF);
		dateVoyage.setDateRetour(dateRetourF2);
		model.addAttribute("dateVoyage", dateVoyage);
		model.addAttribute("destinationId", idDate);
		return "modifier-date";
	}

	/**
	 * @param dateAller    de type Date.
	 * @param dateRetour   de type Date.
	 * @param nbrePlaces   de type int.
	 * @param prixHT       de type float.
	 * @param idDateVoyage de type long.
	 * @param model        de type Model.
	 * @return la page HTML destinationdetails.
	 * @throws ParseException
	 */
	@PostMapping("modifierdate2")
	public String modifyDatesVoyage2(@RequestParam(name = "dateAller") String dateAller,
			@RequestParam(name = "dateRetour") String dateRetour, @RequestParam(name = "nbrePlaces") int nbrePlaces,
			@RequestParam(name = "prixHT") float prixHT, @RequestParam(name = "idDateVoyage") long idDateVoyage,
			Model model) throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date dateAllerF = fmt.parse(dateAller);
		Date dateRetourF = fmt.parse(dateRetour);
		DatesVoyage dateVoyageTemp = datesVoyageRepo.findById(idDateVoyage).get();
		dateVoyageTemp.setDateAller(dateAllerF);
		dateVoyageTemp.setDateRetour(dateRetourF);
		dateVoyageTemp.setNbrePlaces(nbrePlaces);
		dateVoyageTemp.setPrixHT(prixHT);
		datesVoyageRepo.save(dateVoyageTemp);
		model.addAttribute("destination", getDestinationByDatesVoyageId(idDateVoyage));
		model.addAttribute("datesVoyages", getDestinationByDatesVoyageId(idDateVoyage).getDatesVoyages());
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
		List<Destination> destinations = destinationRepo.getValidDestinations();
		model.addAttribute("destinations", destinations);
		return "destinations";
	}

	/**
	 * @param datesVoyage de type DatesVoyage
	 * @return une confirmation de type String suite à la creation d'une nouvelle
	 *         date de voyage.
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
	 * @return une destination de type Destination correspondant à la date de voyage
	 *         selectionnee.
	 */
	@GetMapping("/dates/destination/{id}")
	public Destination getDestinationByDatesVoyageId(@PathVariable("id") long DatesVoyageId) {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		for (Destination d : destinations) {
			List<DatesVoyage> datesvoyages = getValidDatesVoyagesByDestinationId(d.getId());
			for (DatesVoyage dv : datesvoyages) {
				if (dv.getId() == (DatesVoyageId)) {
					return d;
				}
			}
		}
		return null;
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
	 * @param id de type long.
	 */
	@PostMapping("/deletedatevoyage")
	public String deleteDatesVoyageById(@RequestParam(name = "id") long id,
			@RequestParam(name = "idDestination") long idDestination, Model model) {
		DatesVoyage datesVoyage = datesVoyageRepo.findById(id).get();
		datesVoyage.setDeleted(true);
		datesVoyageRepo.save(datesVoyage);

		List<DatesVoyage> datesVoyages = new ArrayList<>();
		Destination destination = destinationRepo.getDestinationWithDatesById(idDestination);
		for (DatesVoyage d : destination.getDatesVoyages()) {
			if (!d.isDeleted())
				datesVoyages.add(d);
		}
		model.addAttribute("destination", destination);
		model.addAttribute("datesVoyages", datesVoyages);
		return "destinationdetails";

	}

	/**
	 * @param nom      de type String.
	 * @param password de type String.
	 * @return is Auth de type boolean.
	 * @throws NoSuchAlgorithmException.
	 */
	@PostMapping("/connexion")
	public String connexionTo(@RequestParam(name = "username") String username,
			@RequestParam(name = "password") String password) throws NoSuchAlgorithmException {
		boolean isAuth;
		String pwdFin = Digest.getStringSha256(password);
		;
		Optional<Commercial> opt = commercialRepo.findByNomAndHashPassword(username, pwdFin);
		return opt.isPresent() ? "home" : "index";
	}

	@PostMapping("/signupform")
	public String signing(Model model) {
		Commercial commercial = new Commercial();
		model.addAttribute("commercial", commercial);
		return "signup";
	}
	
	
	@GetMapping("/signupform1")
	public String signing1(Model model1) {
		Commercial commercial = new Commercial();
		model1.addAttribute("commercial", commercial);
		return "signup";
	}

	/**
	 * @param nom      de type String.
	 * @param password de type String.
	 * @return isAuth de type boolean.
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/confirmsignup")
	public String createCommercial(@RequestParam(name = "username") String username,
			@RequestParam(name = "password") String password, Model model) throws NoSuchAlgorithmException {
		Optional<Commercial> commercial = commercialRepo.findByUsername(username);
		boolean isAuth = !commercial.isPresent() ? true : false;
		if (!commercial.isPresent()) {
			commercialRepo.createNewCommercial(username, password);
			Optional<Commercial> commercial2 = commercialRepo.findByUsername(username);

		}
		return !commercial.isPresent() ? "home" : "signup";
	}

	/**
	 * @param model de type Model.
	 * @return le formulaire d'upload des images de type page HTML.
	 * @throws IOException
	 */
	@PostMapping("/images")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll()
				.map(path -> MvcUriComponentsBuilder
						.fromMethodName(BackOfficeRestController.class, "serveFile", path.getFileName().toString())
						.build().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}

	/**
	 * @param filename
	 * @return
	 */
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	/**
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("/images2")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "home";
	}

	/**
	 * @param exc
	 * @return
	 */
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

	/**
	 * @param model
	 * @return
	 */
	@GetMapping("backtodestinations")
	public String getBacktoDestinationsPage(Model model) {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		model.addAttribute("destinations", destinations);
		return "destinations";
	}
	
	@PostMapping("backtodestinations")
	public String getBacktoDestinationsPage2(Model model) {
		List<Destination> destinations = destinationRepo.getValidDestinations();
		model.addAttribute("destinations", destinations);
		return "destinations";
	}

	/**
	 * @param idDateVoyage de type long.
	 * @param model        de type Model.
	 * @return la page HTML destinationdetails.
	 */
	@PostMapping("backtodestinationdetails")
	public String getBackToDestinationDetailsPage(@RequestParam("idDateVoyage") long idDateVoyage, Model model) {
		model.addAttribute("destination", getDestinationByDatesVoyageId(idDateVoyage));
		model.addAttribute("datesVoyages", getDestinationByDatesVoyageId(idDateVoyage).getDatesVoyages());
		return "destinationdetails";
	}
	
	@GetMapping("backtodestinationdetails")
	public String getBackToDestinationDetailsPage2(@RequestParam("idDateVoyage") long idDateVoyage, Model model) {
		model.addAttribute("destination", getDestinationByDatesVoyageId(idDateVoyage));
		model.addAttribute("datesVoyages", getDestinationByDatesVoyageId(idDateVoyage).getDatesVoyages());
		return "destinationdetails";
	}

	/**
	 * @return la page displayImage de type HTML.
	 */
	@RequestMapping(value = "/displayimage", method = RequestMethod.GET)
	public String index() {
		return "displayImage";
	}

}
