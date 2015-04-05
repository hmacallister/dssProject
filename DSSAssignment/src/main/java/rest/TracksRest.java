package rest;

import java.text.ParseException;
import java.util.ArrayList;
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

import services.TrackService;
import entities.Track;
import entities.User;

@Path("/tracks")
@Stateless
public class TracksRest {

	@EJB
	private TrackService service;
	

	@GET
	@Path("/getlibrary/{libraryID}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String[]> getPlaylistByTitle(@PathParam("libraryID") String libraryID) throws ParseException{
		List<Track> tracklist = service.getTracksByUser(libraryID);	
		ArrayList<String[]> aList = new ArrayList<String[]>();
		for(Track track : tracklist){
			String[] str = {track.getId().toString(), track.getTitle(), track.getArtist(), track.getAlbum(), track.getGenre(), track.getTrackId(), track.getUser().getUsername()};
			aList.add(str);
		}
		return aList;	
	}
	
	@GET
	@Path("/getsearch/{searchTerm}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String[]> getTracksSearch(@PathParam("searchTerm") String searchTerm) throws ParseException{
		List<Track> tracklist = service.getTracksSearch(searchTerm);
		ArrayList<String[]> aList = new ArrayList<String[]>();
		if(tracklist.isEmpty()){
			String[] str = {"No Match Found", "", "", "", "", "", ""};
			aList.add(str);
			return aList;
		}

		for(Track track : tracklist){
			String[] str = {track.getId().toString(), track.getTitle(), track.getArtist(), track.getAlbum(), track.getGenre(), track.getTrackId(), track.getUser().getUsername()};
			aList.add(str);
		}
		return aList;	
	}
	
	@POST
	@Path("/updatetrack")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateTrack(Track track) {
		 service.updateTrack(track);
	}
	
	@DELETE
	@Path("/deletetrack")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteTrack(Track track){
		service.deleteTrack(track);
	}
	

}
