package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

	@Column(name = "title")
	private String title;
	
	@Column(name="album")
	private String album;
	
	@Column(name="artist")
	private String artist;
	
	@Column(name="genre")
	private String genre;
	
	@JoinColumn (name="user_id", referencedColumnName="id", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	/*
	@JoinColumn(name = "tracks", referencedColumnName = "id", nullable = true)
	@ManyToOne
	private List<Track> trackTitles = new ArrayList<Track>();
	*/

	public Track() {
	}
	

	public Track(String title, String album, String artist, String genre) {
		super();
		this.title = title;
		this.album = album;
		this.artist = artist;
		this.genre = genre;
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

	/*
	public User getLibrary() {
		return library;
	}

	public void setLibrary(User library) {
		this.library = library;
	}
	*/
	
	

	/*
	public Album getDisc() {
		return disc;
	}

	public void setDisc(Album disc) {
		this.disc = disc;
	}
	*/

	/*
	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
	*/

}
