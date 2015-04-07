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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.jpa.JPAPlaylistDAO;
import services.PlaylistService;
import services.TrackService;
import entities.Playlist;
import entities.Track;

@Path("/tracks")
@Stateless
public class TracksRest {

	@EJB
	private TrackService service;
	@EJB
	private PlaylistService playlistService;
	private final Logger log = LoggerFactory.getLogger(TracksRest.class);

	@GET
	@Path("/getlibrary/{libraryID}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String[]> getPlaylistByTitle(
			@PathParam("libraryID") String libraryID) throws ParseException {
		List<Track> tracklist = service.getTracksByUser(libraryID);
		ArrayList<String[]> aList = new ArrayList<String[]>();
		for (Track track : tracklist) {
			String[] str = { track.getId().toString(), track.getTitle(),
					track.getArtist(), track.getAlbum(), track.getGenre(),
					track.getTrackId(), track.getUser().getUsername() };
			aList.add(str);
		}
		return aList;
	}

	@GET
	@Path("/getsearch/{searchTerm}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String[]> getTracksSearch(
			@PathParam("searchTerm") String searchTerm) throws ParseException {
		List<Track> tracklist = service.getTracksSearch(searchTerm);
		ArrayList<String[]> aList = new ArrayList<String[]>();
		if (tracklist.isEmpty() || tracklist == null) {
			String[] str = { "No Match Found", "", "", "", "", "", "" };
			aList.add(str);
			return aList;
		}

		for (Track track : tracklist) {
			String[] str = { track.getId().toString(), track.getTitle(),
					track.getArtist(), track.getAlbum(), track.getGenre(),
					track.getTrackId(), track.getUser().getUsername() };
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
	public void deleteTrack(Track track) {
		try {
			Track t = service.getTrackById(track.getId());
			//log.info("track id passed is: "+track.getId());
			if (t != null) {
				List<Playlist> playlists = playlistService.getPlaylistsByUser(t.getUser().getLibraryPersistentID());
				List<Playlist> newPlaylists = new ArrayList<Playlist>();
				for (Playlist p : playlists) {
					//log.info("playlist checked is: "+p.getTitle());
					boolean inPlaylist = false;
					List<Track> playlistTracks = new ArrayList<Track>();
					for (Track pTrack : p.getTrackTitles()) {
						if (pTrack.getId() == t.getId()) {
							inPlaylist = true;
							//log.info("track to delete is in playlist: "+pTrack.getId());
						} else {
							playlistTracks.add(pTrack);
							//log.info("other tracks are: "+pTrack.getId());
						}
					}
					if (inPlaylist == true) {
						Playlist updatedPlaylist = new Playlist(p.getId(), p.getTitle(), p.getUserFK(), playlistTracks);
						updatedPlaylist.setTrackTitles(playlistTracks);
						//log.info("playlist added to new list: "+p.getTitle());
						newPlaylists.add(updatedPlaylist);
					}
				}// end for playlists
				if(service.deleteTrack(t) == true){
					for (Playlist np : newPlaylists) {
						playlistService.addPlaylist(np);
					}
				}

			}
			
		} catch (Exception e) {
			log.info("delete failed");
		}
	}

}
