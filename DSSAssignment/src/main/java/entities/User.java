package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity 
@Table(name="users")
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="libraryPersistentID")
	private String libraryPersistentID;
	

	public User() {
	}

	public User(String username, String password, String libraryPersistentID) {
		super();
		this.username = username;
		this.password = password;
		this.libraryPersistentID = libraryPersistentID;
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/*
	public void addTrack(Track t) {
		t.setLibrary(this);
		trackTitles.add(t);
	}
	*/
	
	public User(int id) {
		this.id=id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getLibraryPersistentID() {
		return libraryPersistentID;
	}

	public void setLibraryPersistentID(String libraryPersistentID) {
		this.libraryPersistentID = libraryPersistentID;
	}
	
	/*

	public List<Track> getTrackTitles() {
		return trackTitles;
	}

	public void setTrackTitles(List<Track> trackTitles) {
		this.trackTitles = trackTitles;
	}
	
	*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((libraryPersistentID == null) ? 0 : libraryPersistentID
						.hashCode());
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
		User other = (User) obj;
		if (libraryPersistentID == null) {
			if (other.libraryPersistentID != null)
				return false;
		} else if (!libraryPersistentID.equals(other.libraryPersistentID))
			return false;
		return true;
	}

	

}