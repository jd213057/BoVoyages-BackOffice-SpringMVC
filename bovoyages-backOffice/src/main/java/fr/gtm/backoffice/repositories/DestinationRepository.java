package fr.gtm.backoffice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.gtm.backoffice.entities.Destination;

/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0
 * Repository de l'entite Destination.
 */
@Repository
public interface DestinationRepository extends JpaRepository <Destination, Long> {
	
	/**
	 * @return une List<Destination> de l'ensemble des destinations non rayées en BDD.
	 */	
	@Query("SELECT d FROM Destination d WHERE d.raye =0")
	List<Destination> getValidDestinations();
	
	/**
	 * @param region de type Destination.
	 * @return une List<Destination> en fonction de la region voulue.
	 */
	@Query("SELECT d FROM Destination d WHERE UPPER(d.region) LIKE :region%")
	List<Destination> findDestinationByRegion(String region);
	
	/**
	 * @param id de type long.
	 * @return une destination de type Destination avec ses dates de voyages.
	 */
	@Query("SELECT d FROM Destination d JOIN FETCH d.datesVoyages WHERE d.id=?1 ")
	Destination getDestinationWithDatesById(long id);
	
	/**
	 * @param id de type long.
	 * @return une destination de type Destination correspondant à l'id en argument de la methode.
	 */
	@Query("SELECT d FROM Destination d WHERE d.id=?1 AND d.raye=0")
	Optional<Destination> findById(long id);
}
