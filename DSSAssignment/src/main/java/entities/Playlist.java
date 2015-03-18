package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name="title")
	private String title;
	
	@JoinColumn(name = "users", referencedColumnName = "id", nullable = true)
	@OneToOne
	private User userFK;
	
	@OneToMany(mappedBy="playlist", cascade={CascadeType.MERGE, CascadeType.PERSIST})
	private List<Track> trackTitles = new ArrayList<Track>();
	

	public Playlist() {
	}

	public Playlist(String title, User userFK, List<Track> trackTitles) {
		super();
		this.title = title;
		this.userFK = userFK;
		this.trackTitles = trackTitles;
	}



	public void addTrack(Track t) {
		t.setPlaylist(this);
		trackTitles.add(t);
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


}
