package fr.gtm.backoffice.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0
 * Classe DatesVoyage, serialisable.
 */
@Entity
@Table(name = "dates_voyages")
@Access(AccessType.FIELD)
public class DatesVoyage implements Serializable {

    /**
     * Identifiant id de type long.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_date_voyage")
    private long id;
    /**
     * Attribut dateAller de type Date.
     */
    @Column(name = "date_depart")
    private Date dateAller;
    /**
     * Attribut dateRetour de type Date.
     */
    @Column(name = "date_retour")
    private Date dateRetour;
    /**
     * Attribut prixHT de type float.
     */
    private float prixHT;
    /**
     * Attribut nbrePlaces de type int.
     */
    @Column(name = "nb_places")
    private int nbrePlaces;
    /**
     * Attribut deleted de type boolean.
     */
    private boolean deleted;
    /**
     * Attribut promotion de type boolean.
     */
    private boolean promotion;

    /**
     * Constructeur par défaaut de DatesVoyage
     */
    public DatesVoyage() {}
    
    /**
     * Constructeur de DatesVoyage.
     * @param id de type long.
     * @param dateAller de type Date.
     * @param dateRetour de type Date.
     * @param prixHT de type float.
     * @param deleted de type boolean.
     * @param promotion de type boolean.
     */
    public DatesVoyage(long id, Date dateAller, Date dateRetour, float prixHT, boolean deleted, boolean promotion) {
		super();
		this.id = id;
		this.dateAller = dateAller;
		this.dateRetour = dateRetour;
		this.prixHT = prixHT;
		this.deleted = deleted;
		this.promotion = promotion;
	}
    
    public DatesVoyage(Date dateAller, Date dateRetour, float prixHT, int nbrePlaces) {
		super();
		this.dateAller = dateAller;
		this.dateRetour = dateRetour;
		this.prixHT = prixHT;
		this.nbrePlaces = nbrePlaces;
		this.deleted = false;
		this.promotion = false;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateAller() {
        return dateAller;
    }

    public void setDateAller(Date dateAller) {
        this.dateAller = dateAller;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public float getPrixHT() {
        return prixHT;
    }

    public void setPrixHT(float prixHT) {
        this.prixHT = prixHT;
    }

    public int getNbrePlaces() {
        return nbrePlaces;
    }

    public void setNbrePlaces(int nbrePlaces) {
        this.nbrePlaces = nbrePlaces;
    }


    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateAller == null) ? 0 : dateAller.hashCode());
		result = prime * result + ((dateRetour == null) ? 0 : dateRetour.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + nbrePlaces;
		result = prime * result + Float.floatToIntBits(prixHT);
		result = prime * result + (promotion ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatesVoyage other = (DatesVoyage) obj;
		if (dateAller == null) {
			if (other.dateAller != null)
				return false;
		} else if (!dateAller.equals(other.dateAller))
			return false;
		if (dateRetour == null) {
			if (other.dateRetour != null)
				return false;
		} else if (!dateRetour.equals(other.dateRetour))
			return false;
		if (deleted != other.deleted)
			return false;
		if (id != other.id)
			return false;
		if (nbrePlaces != other.nbrePlaces)
			return false;
		if (Float.floatToIntBits(prixHT) != Float.floatToIntBits(other.prixHT))
			return false;
		if (promotion != other.promotion)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  dateAller + ", prixHT=" + prixHT +" euros";
	}
	
	
    
}
