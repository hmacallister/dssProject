package entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity 
@Table(name="playlists")
public class Playlist implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="title")
	private String title;
	
	@JoinColumn(name = "users", referencedColumnName = "id", nullable = true)
	@OneToOne(cascade =CascadeType.ALL, fetch= FetchType.EAGER, orphanRemoval=true)
	private User userFK;

	@JoinColumn(name = "tracks", referencedColumnName = "id", nullable = true)
	@ManyToMany(fetch= FetchType.EAGER)
	private List<Track> trackTitles;

	public Playlist() {
	}

	public Playlist(String title, User userFK) {
		this.title = title;
		this.userFK = userFK;
	}

	public Playlist(String title) {
		this.title = title;
	}

	public Playlist(Integer id, String title, User userFK, List<Track> trackTitles) {
		this.id = id;
		this.title = title;
		this.userFK = userFK;
		this.trackTitles = trackTitles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUserFK() {
		return userFK;
	}

	public void setUserFK(User userFK) {
		this.userFK = userFK;
	}


	public List<Track> getTrackTitles() {
		return trackTitles;
	}

	public void setTrackTitles(List<Track> trackTitles) {
		this.trackTitles = trackTitles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((userFK == null) ? 0 : userFK.hashCode());
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
		Playlist other = (Playlist) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userFK == null) {
			if (other.userFK != null)
				return false;
		} else if (!userFK.equals(other.userFK))
			return false;
		return true;
	}

}