package fr.gtm.backoffice.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0
 * Classe Destination, serialisable. 
 */
@Entity
@Table(name = "destinations")
@Access(AccessType.FIELD)
public class Destination implements Serializable {

    /**
     * Identifiant id de type long. 
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_destination")
    private long id;
    /**
     * Attribut region de type String.
     */
    private String region;
    /**
     * Attribut description de type String.
     */
    private String description;
    /**
     * Attribut raye de type boolean.
     */
    @Column(name = "deleted")
    private boolean raye;

    /**
     * Attribut datesVoyages de type List<DatesVoyage>.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_destination")
    private List<DatesVoyage> datesVoyages = new ArrayList<DatesVoyage>();
    
    /**
     * Attribut images de type List<Image>. 
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.LAZY)
    @JoinColumn(name="fk_destination")
    private List<Image> images=new ArrayList<Image>();

    /**
     * @param region de type String.
     * @param description de type String.
     * @param raye de type boolean.
     * Constructeur de Destination servant à DestinationDTO.
     */
    public Destination(String region, String description, boolean raye) {
    	this.region = region;
		this.description = description;
		this.raye = raye;	
    }
    
    
    
    /**
     * @param region de type String.
     * @param description de type String.
     * @param raye de type boolean.
     * @param datesVoyages de type DatesVoyage.
     * Constructeur de Destination.
     */
    public Destination(String region, String description, boolean raye, List<DatesVoyage> datesVoyages) {
		super();
		this.region = region;
		this.description = description;
		this.raye = raye;
		this.datesVoyages = datesVoyages;
	}



	/**
     * Constructeur par défaut de Destination.
     */
    public Destination() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRaye() {
        return raye;
    }

    public void setRaye(boolean raye) {
        this.raye = raye;
    }

    public List<DatesVoyage> getDatesVoyages() {
        return datesVoyages;
    }

    public void setDatesVoyages(List<DatesVoyage> datesVoyages) {
        this.datesVoyages = datesVoyages;
    }
    
    

	public List<Image> getImages() {
		return images;
	}



	public void setImages(List<Image> images) {
		this.images = images;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datesVoyages == null) ? 0 : datesVoyages.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((images == null) ? 0 : images.hashCode());
		result = prime * result + (raye ? 1231 : 1237);
		result = prime * result + ((region == null) ? 0 : region.hashCode());
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
		Destination other = (Destination) obj;
		if (datesVoyages == null) {
			if (other.datesVoyages != null)
				return false;
		} else if (!datesVoyages.equals(other.datesVoyages))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (images == null) {
			if (other.images != null)
				return false;
		} else if (!images.equals(other.images))
			return false;
		if (raye != other.raye)
			return false;
		if (region == null) {
			if (other.region != null)
				return false;
		} else if (!region.equals(other.region))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Destination [id=" + id + ", region=" + region + ", description=" + description + ", raye=" + raye
				+ ", datesVoyages=" + datesVoyages + ", images=" + images + "]";
	}



	


}
