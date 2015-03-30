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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import services.UserService;
import dao.TrackDAO;
import entities.Playlist;
import entities.Track;
import entities.User;

@Path("/users")
@Stateless
public class UsersRest {

	@EJB
	private UserService service;
	
	@EJB
	private TrackDAO trackDAO;

	@GET
	@Path("/getallusers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getAllUsers() {
		return service.getUsers();
	}
	
	@POST
	@Path("/getuserbyname")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User getPlaylistsByUser(User user) {
		return service.getUserByName(user.getUsername());
	}

	
	@POST
	@Path("/getuser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(User user) {
		return service.getUser(user);
	}
	
	@POST
	@Path("/getuserbyid")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserById(User user) {
		return service.getUserById(user);
	}
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User addUser(User user){
		 return service.addUser(user);
	}
	
	@POST
	@Path("/updateuser")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateUser(User user) {
		 service.updateUser(user);
	}
	
	@DELETE
	@Path("/deleteuser")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteUser(User user){
		service.deleteUser(user);
	}
		

}
