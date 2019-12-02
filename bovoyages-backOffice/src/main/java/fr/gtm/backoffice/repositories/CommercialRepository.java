package fr.gtm.backoffice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.gtm.backoffice.entities.Commercial;

/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0 Controller de type REST de BoVoyages-BackOffice. Repository de
 *          l'entite Commercial.
 */
@Repository
public interface CommercialRepository extends JpaRepository<Commercial, Long> {

	/**
	 * @param nom de type String.
	 * @return le mot de passe chiffré du client correspondant.
	 */
	@Query(value = "SELECT digest from commerciaux WHERE commerciaux = ?1", nativeQuery = true)
	String getValues(String nom);

	/**
	 * @param nom      de type String.
	 * @param password de type String. Création d'un nouveau commercial.
	 */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO commerciaux (nom, digest) VALUES(?1, SHA2(?2,256))", nativeQuery = true)
	void createNewCommercial(String nom, String password);

	/**
	 * @param nom de type String
	 * @return un Commercial si il existe en BDD.
	 */
	@Query(value="SELECT c FROM Commercial c WHERE c.username=?1")
	public Optional<Commercial> findByUsername(String username);

	/**
	 * @param username de type String.
	 * @param password de type String.
	 * @return un Client si il existe en BDD.
	 */
	@Query(value = "Select * From commerciaux c WHERE c.username = ?1 and c.digest =?2", nativeQuery = true)
	Optional<Commercial> findByNomAndHashPassword(String username, String pwdFin);

}
