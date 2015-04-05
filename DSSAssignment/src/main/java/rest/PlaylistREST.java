package rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.jpa.JPATrackDAO;
import services.PlaylistService;
import entities.Playlist;
import entities.Track;

@Path("/playlists")
@Stateless
public class PlaylistREST {

	@EJB
	private PlaylistService service;
	
	private final Logger log = LoggerFactory.getLogger(PlaylistREST.class);
	
	//works
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Playlist> getAllPlaylists() {
		return service.getAllPlaylists();
	}
	
	@GET
	@Path("playlistsbyuser/{user}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Playlist> getPlaylistsByUser(@PathParam("user") String user) {
		return service.getPlaylistsByUser(user);
	}
	
	@GET
	@Path("sort/{order}")
	//@Produces(MediaType.APPLICATION_JSON)
	public void sortPlaylist(@PathParam("order") String order) {
		Playlist playlist = new Playlist();
		//log.info("received from json: "+order);
		List<Track> trackList = new ArrayList<Track>(); 
		int id = Integer.parseInt(order.substring(0, 1));
		//log.info("playlist id is: "+id);
		playlist.setId(id);
		String[] trackIds = order.split("id");
		for(int i=2; i<trackIds.length;i++){
			try{
				int trackId = Integer.parseInt(trackIds[i]);
				//log.info("track id is: "+trackId);
				Track track = new Track(trackId);
				trackList.add(track);
			}
			catch(Exception e){
				log.info("couldn't sort: "+trackIds[i]);
			}

		}
		playlist.setTrackTitles(trackList);
		//return service.getPlaylistsByUser(user);
		service.updatePlaylist(playlist);
	}
	
	@GET
	@Path("/getplaylistbyId/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String[]> getPlaylistByTitle(@PathParam("title") String title) {
		ArrayList<String[]> aList = new ArrayList<String[]>();
		try{
			int id = Integer.parseInt(title);
			Playlist playlist =  service.getPlaylistById(id);
			if(playlist.getTitle().equals("empty")){
				String[] str = {"Playlist empty", "", "", "", "", "", ""};
				aList.add(str);
				return aList;
			}
			List<Track> tracklist= playlist.getTrackTitles();
			for(Track track : tracklist){
				String[] str = {track.getId().toString(), track.getTitle(), track.getArtist(), track.getAlbum(), track.getGenre(), track.getTrackId(), track.getUser().getUsername()};
				aList.add(str);
			}
			return aList;
		}
		catch(Exception e){
			log.info("number exception: "+title);
		}
		String[] str = {"Playlist empty", "", "", "", "", "", ""};
		aList.add(str);
		return aList;
	}
	
	@POST
	@Path("/updateplaylist")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updatePlaylist(Playlist playlist) {
		 service.updatePlaylist(playlist);
	}
	
	@DELETE
	@Path("/deletetrackfromplaylist")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteTrack(Track track){
		service.deleteTrackFromPlaylist(track);
	}

}
