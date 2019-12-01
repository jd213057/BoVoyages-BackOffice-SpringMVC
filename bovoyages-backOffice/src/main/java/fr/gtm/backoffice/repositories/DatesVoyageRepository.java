package fr.gtm.backoffice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.gtm.backoffice.entities.DatesVoyage;
import fr.gtm.backoffice.entities.Destination;

/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0 
 * Repository de l'entite DatesVoyage.
 */
@Repository
public interface DatesVoyageRepository extends JpaRepository<DatesVoyage, Long> {

	/**
	 * @return List<DatesVoyage> non raye en BDD.
	 */
	@Query("SELECT d FROM DatesVoyage d WHERE d.deleted =0")
	List<DatesVoyage> getValidDestinations();

	/**
	 * @param id de type long.
	 * @return une destination de type Destination correspondant à l'id en argument
	 *         de la methode.
	 */
	@Query("SELECT d FROM DatesVoyage d WHERE d.id=?1 AND d.deleted=0")
	Optional<DatesVoyage> findById(long id);

}
