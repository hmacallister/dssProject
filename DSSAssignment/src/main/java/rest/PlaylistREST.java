package rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
	@Path("/getplaylistbyId/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String[]> getPlaylistByTitle(@PathParam("title") String title) {
		int id = Integer.parseInt(title);
		Playlist playlist =  service.getPlaylistById(id);
		ArrayList<String[]> aList = new ArrayList<String[]>();
		if(playlist.getTitle().equals("empty")){
			String[] str = {"Playlist empty", "", "", "", "", "", ""};
			aList.add(str);
			return aList;
		}
		Collection<Track> tracklist= playlist.getTrackTitles();
		for(Track track : tracklist){
			String[] str = {track.getId().toString(), track.getTitle(), track.getArtist(), track.getAlbum(), track.getGenre(), track.getTrackId(), track.getUser().getUsername()};
			aList.add(str);
		}
		return aList;	
	}
	
	@DELETE
	@Path("/deletetrackfromplaylist")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteTrack(Track track){
		service.deleteTrackFromPlaylist(track);
	}

}
