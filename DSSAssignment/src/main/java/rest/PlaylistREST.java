package rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import services.PlaylistService;
import entities.Playlist;
import entities.Track;

@Path("/playlists")
@Stateless
public class PlaylistREST {

	@EJB
	private PlaylistService service;
	
	//works
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Playlist> getAllPlaylists() {
		return service.getAllPlaylists();
	}
	
	@GET
	@Path("playlistsbyuser/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Playlist> getPlaylistsByUser(@PathParam("user") String user) {
		return service.getPlaylistsByUser(user);
	}
	
	@GET
	@Path("/getplaylistbytitle/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String[]> getPlaylistByTitle(@PathParam("title") String title) {
		Playlist playlist =  service.getPlaylistByTitle(title);
		List<Track> tracklist= playlist.getTrackTitles();
		ArrayList<String[]> aList = new ArrayList<String[]>();
		for(Track track : tracklist){
			String[] str = {track.getId().toString(), track.getTitle(), track.getArtist(), track.getAlbum(), track.getGenre(), track.getTrackId(), track.getUser().getUsername()};
			aList.add(str);
		}
		return aList;	
	}

}
