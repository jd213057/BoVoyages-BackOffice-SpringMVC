package fr.gtm.backoffice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.gtm.backoffice.entities.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image,String> {

}
