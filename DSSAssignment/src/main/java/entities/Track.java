package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

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
	
	@JoinColumn (name="cd_id", referencedColumnName="id", nullable = false)
	@ManyToOne
	private Album disc;
	
	/*
	@JoinColumn (name="playlist", referencedColumnName="id", nullable = false)
	@OneToMany
	private List<Playlist> playlists = new ArrayList<Playlist>();
	*/

	public Track() {
	}

	public Track(String title, Album disc) {
		this.title = title;
		this.disc = disc;
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

	public Album getDisc() {
		return disc;
	}

	public void setDisc(Album disc) {
		this.disc = disc;
	}

	/*
	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
	*/

}
