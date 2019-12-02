package fr.gtm.backoffice.entities;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name="commerciaux")
public class Commercial implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_commercial")
	private long id;
    private String username;
    @Column(name="password")
    private String digest;
    @Column(name="isauth")
    private boolean isAuth;
    
    public Commercial() {}

	public Commercial(long id, String username, String digest) {
		super();
		this.id = id;
		this.username = username;
		this.digest = digest;
	}
	
	

	public Commercial(String username, String digest) {
		super();
		this.username = username;
		this.digest = digest;
		this.isAuth = false;
	}

	public Commercial(String username, boolean isAuth) {
		super();
		this.username = username;
		this.isAuth = isAuth;
	}

	public Commercial(String username) {
		super();
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	

	public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((digest == null) ? 0 : digest.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (isAuth ? 1231 : 1237);
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		Commercial other = (Commercial) obj;
		if (digest == null) {
			if (other.digest != null)
				return false;
		} else if (!digest.equals(other.digest))
			return false;
		if (id != other.id)
			return false;
		if (isAuth != other.isAuth)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Commercial [username=" + username + "]";
	}
    
    

}
