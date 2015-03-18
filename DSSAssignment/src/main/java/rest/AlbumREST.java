package rest;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import services.AlbumService;
import entities.Album;

@Path("/albums")
@Stateless
public class AlbumREST {

	@EJB
	private AlbumService service;

	//works
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Album> getAllDiscs() {
		return service.getCatalog();
	}

	//works
	@GET
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Album getCompactDisc(@PathParam("id") int id) {
		return service.getCompactDisc(id);
	}
	
	//works
	@GET
	@Path("/title/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public Album getCompactDiscByTitle(@PathParam("title") String title) {
		return service.getCDByTitle(title);
	}
	
	//works
	@GET
	@Path("/artist/{artist}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Album> getAllDiscsByArtist(@PathParam("artist") String artist) {
		return service.getCDsByArtist(artist);
	}

	//doesn't work
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void addCompactDisc(Album disc) {
		service.addCDToCatalog(disc);
	}
	
	//works
	@DELETE
	@Path("/{id}")
	public void deleteCompactDisc(@PathParam("id") int id) {
		service.deleteCompactDisc(id);
	}
	
	//doesn't work
	@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateCompactDisc(Album updatedDisc) {
		service.updateCompactDisc(updatedDisc);
	}

}
