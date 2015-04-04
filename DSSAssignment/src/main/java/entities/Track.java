package entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tracks")
public class Track implements Serializable {

  	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	@Column(name="id")
	private Integer id;
	
  	@Column(name="track_id")
	private String trackId;

	@Column(name = "title")
	private String title;
	
	@Column(name="album")
	private String album;
	
	@Column(name="artist")
	private String artist;
	
	@Column(name="genre")
	private String genre;
	
	@JoinColumn (name="user_id", referencedColumnName="id", nullable = true)
	@ManyToOne(cascade=CascadeType.ALL,fetch= FetchType.EAGER)
	private User user;
	
	@ManyToMany(cascade =CascadeType.ALL, mappedBy="trackTitles")
	private Collection<Playlist> playlists;

	/*
	@JoinColumn(name = "tracks", referencedColumnName = "id", nullable = true)
	@ManyToOne
	private List<Track> trackTitles = new ArrayList<Track>();
	*/

	public Track() {
	}
	

	public Track(Integer id) {
		this.id = id;
	}
	
	public Track(String title, String album, String artist, String genre, String trackId) {
		this.title = title;
		this.album = album;
		this.artist = artist;
		this.genre = genre;
		this.trackId = trackId;
	}
	
	public Track(String title, String album, String artist, String genre, Integer id) {
		this.title = title;
		this.album = album;
		this.artist = artist;
		this.genre = genre;
		this.id = id;
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

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String getTrackId() {
		return trackId;
	}


	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((trackId == null) ? 0 : trackId.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Track other = (Track) obj;
		if (trackId == null) {
			if (other.trackId != null)
				return false;
		} else if (!trackId.equals(other.trackId))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	

}