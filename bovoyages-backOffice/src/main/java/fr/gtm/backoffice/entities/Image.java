package fr.gtm.backoffice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0
 * Classe Image, serialisable.
 */
@Entity
@Table(name = "images")
public class Image { 
	/**
	 * Identifiant de type 
	 */
	@Id
	@Column(name = "image")
	private String image;
	/**
	 * Attribut fkDestination de type long.
	 */
	@Column(name = "fk_destination")
	private long fkDestination;
	/**
	 * Constructeur par défaut de Image.
	 */
	public Image() {}
	/**
	 * @param image de type String.
	 * @param fkDestination de type long.
	 */
	public Image(String image,long fkDestination) {
		this.image=image;
		this.fkDestination=fkDestination;
	}
	/**
	 * @return image de type String.
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image de type String.
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return fkDestination de type long.
	 */
	public long getFkDestination() {
		return fkDestination;
	}

	/**
	 * @param fkDestination de type long.
	 */
	public void setFkDestination(long fkDestination) {
		this.fkDestination = fkDestination;
	}

	/**
	 * @param args de type String[].
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}