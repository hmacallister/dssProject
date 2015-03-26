package dao.jpa;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dao.TrackDAO;
import dao.UserDAO;
import entities.Track;
import entities.User;

@Stateless
@Local
public class JPATrackDAO implements TrackDAO{
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void addTracks(List<Track> tracks) {
		for(Track track: tracks){
			em.merge(track);
		}
		
	}

	public Collection<User> getAllTracks() {
		Query query = em.createQuery("from Track");
		List<User> result = query.getResultList();
		return result;
	}

	@Override
	public Track getTrack(String trackId) {
		Query query = em.createQuery("from Track t where t.trackId = :trackId");
		query.setParameter("trackId", trackId);
		List<Track> result = query.getResultList();
		if(result.isEmpty()){
			Track t = new Track("error", "error", "error", "error", trackId);
			em.merge(t);
			return  t;
		}
		return result.get(0);
	}

	@Override
	public List<Track> getTracksByUser(String userID) {
		Query userQuery = em.createQuery("from User u where u.libraryPersistentID = :userID");
		userQuery.setParameter("userID", userID);
		List<User> userresult = userQuery.getResultList();
		User user = userresult.get(0);
		Query query = em.createQuery("from Track t where t.user = :user");
		query.setParameter("user", user);
		List<Track> result = query.setMaxResults(100).getResultList();
		if(result.isEmpty()){
			return null;
		}
		return result;
	}

	/*
	
	public User getUser(User user) {
		Query q = em.createQuery("from User");
		List<User> res = q.getResultList();
		if(res.isEmpty()){
			User admin = new User("admin", "admin", "0");
			em.merge(admin);
		}
		Query query = em.createQuery("from User u where u.username = :username and u.password = :password");
		query.setParameter("username", user.getUsername());
		query.setParameter("password", user.getPassword());
		List<User> result = query.getResultList();
		
		if(result.isEmpty()){
			return new User("ERROR", "ERROR", "-1");
		}
		
		return result.get(0);
	}
	
	public User addUser(User user){
		Query query = em.createQuery("from User");
		List<User> users = query.getResultList();
		if (!users.contains(user)){
			em.merge(user);
			return user;
		}
		return null;
		//return "User Not Added";
	}


	public void updateUser(User user) {

		Query query = em.createQuery("from User");
		List<User> users = query.getResultList();
		for(User u : users){
			if(u.getId().equals(user.getId())){
				u.setPassword(user.getPassword());
				u.setUsername(user.getUsername());
				u.setLibraryPersistentID(user.getLibraryPersistentID());
				em.merge(u);
				break;
			}
		}
	}


	@Override
	public void deleteUser(User user) {
		User deletedUser = em.find(User.class, user.getId());
		em.remove(deletedUser);
		
	}


	@Override
	public User getUserById(User user) {
		Query query = em.createQuery("from User u where u.id = :id");
		query.setParameter("id", user.getId());
		List<User> result = query.getResultList();
		return result.get(0);
	}


	*/
	

}
