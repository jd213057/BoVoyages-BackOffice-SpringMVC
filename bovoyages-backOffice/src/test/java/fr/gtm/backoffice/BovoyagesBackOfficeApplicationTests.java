package fr.gtm.backoffice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.gtm.backoffice.entities.Commercial;
import fr.gtm.backoffice.entities.DatesVoyage;
import fr.gtm.backoffice.entities.Destination;
import fr.gtm.backoffice.repositories.CommercialRepository;
import fr.gtm.backoffice.repositories.DestinationRepository;

@SpringBootTest
class BovoyagesBackOfficeApplicationTests {
	
	
	@Autowired
	CommercialRepository commercialRepository;
	
	@Autowired
	DestinationRepository destinationRepository;
	
	

	
	@Test
	public void testCreateNewCommercial(){

		commercialRepository.createNewCommercial("Alphonsefgfggf45fbvvc1", "dfghjjk");
	Optional<Commercial> commercial = commercialRepository.findByUsername("Alphonsefgfggf45fbvvc1");

	if (commercial.isPresent()) {
	assertNotNull(commercial);
	assertEquals("Alphonsefgfggf45fbvvc1",commercial.get().getUsername());
	}
	commercialRepository.delete(commercial.get());
	}
	
	
	@Test
	public void testFindByNomAndHashPassword(){

		commercialRepository.createNewCommercial("Alphonse4456fdfvffdfdbvb3", "abc");
	Optional<Commercial> commercial = commercialRepository.findByNomAndHashPassword("Alphonse4456fdfvffdfdbvb3", "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad ");
    System.out.println(commercial.toString());
	if (commercial.isPresent()) {
	assertNotNull(commercial);
	assertEquals("Alphonse4456fdfvffdfdbvb3",commercial.get().getUsername());
	}
	commercialRepository.delete(commercial.get());
	}
	
	@Test
	public void testGetValidDestinations() {
			Destination destination1 = new Destination("region1","description1",false);
			Destination destination2 = new Destination("region2","description2",true);

			destination1 = destinationRepository.save(destination1);
			destination2 = destinationRepository.save(destination2);

			assertNotNull(destination1);
	  		assertNotNull(destination2);

			List<Destination> destinations = destinationRepository.getValidDestinations();
			assertTrue(destinations.size()>0);
			
			for (Destination d : destinations) {
				assertTrue(!d.isRaye());
				System.out.println(">>>>>>>>>>>>"+d.isRaye());
			}

		}
	
	@Test
	public void testGetDestinationWithDatesById() {
		
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dateAller1 = null;
			Date dateRetour1 = null;
			Date dateAller2 = null;
			Date dateRetour2 = null;
			try {
				dateAller1 = fmt.parse("1997-12-28");
				dateRetour1 = fmt.parse("2018-12-28");
				dateAller2 = fmt.parse("2019-12-17");
				dateRetour2 = fmt.parse("2019-12-17");
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		   System.out.println("daaaaaaaaaaaaate1a : " + dateAller1);
		   System.out.println("daaaaaaaaaaaaate1r : " + dateRetour1);
		   System.out.println("daaaaaaaaaaaaate2a : " + dateAller2);
		   System.out.println("daaaaaaaaaaaaate2r : " + dateRetour2);
		   
			DatesVoyage datesVoyage1 = new DatesVoyage(dateAller1,dateRetour1,20.5F, 15);
			DatesVoyage datesVoyage2 = new DatesVoyage(dateAller2,dateRetour2,21.5F, 15);

			List<DatesVoyage> datesVoyages = new ArrayList<DatesVoyage>();
			datesVoyages.add(datesVoyage1);
			datesVoyages.add(datesVoyage2);

			Destination destination1 = new Destination("region5","description4",false);
			Destination destination2 = new Destination("region6","description4",false);

			destination2.setDatesVoyages(datesVoyages);

			Destination destination1bis = destinationRepository.save(destination1);
			Destination destination2bis = destinationRepository.save(destination2);

			assertNull(destinationRepository.getDestinationWithDatesById(destination1bis.getId()));
			assertEquals(destination2bis.getId(), destination2.getId());
			assertTrue(destination2.getDatesVoyages().size() >0);
		

		}
	
	@Test
	public void testFindDestinationByRegion() {
			
			Destination destination1 = new Destination("region3","description10",false);
			Destination destination2 = new Destination("region3","description11",true);
			Destination destination3 = new Destination("region3","description11",true);

			destination1 = destinationRepository.save(destination1);
			destination2 = destinationRepository.save(destination2);
			destination3 = destinationRepository.save(destination3);

			assertNotNull(destination1);
	  		assertNotNull(destination2);
			assertNotNull(destination3);

			List<Destination> destinations = destinationRepository.findDestinationByRegion("region3");

			for(Destination d : destinations)
			{
				assertEquals("region3",d.getRegion());
			}


		}

}
