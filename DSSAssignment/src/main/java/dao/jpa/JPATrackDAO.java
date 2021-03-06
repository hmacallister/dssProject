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
			em.persist(track);
		}
		
	}

	public Collection<User> getAllTracks() {
		Query query = em.createQuery("from Track");
		List<User> result = query.getResultList();
		return result;
	}

	/*
	
	public User getUser(User user) {
		Query q = em.createQuery("from User");
		List<User> res = q.getResultList();
		if(res.isEmpty()){
			User admin = new User("admin", "admin", "0");
			em.persist(admin);
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
			em.persist(user);
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
