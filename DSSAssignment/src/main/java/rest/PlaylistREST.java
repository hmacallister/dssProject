package rest;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import services.PlaylistService;
import entities.Playlist;

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

}
