package dao.jpa;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dao.UserDAO;
import entities.User;

@Stateless
@Local
public class JPAUserDAO implements UserDAO {

	@PersistenceContext
	private EntityManager em;

	public Collection<User> getAllUsers() {
		Query query = em
				.createQuery("from User u, Track t where t.user = u.id");
		List<User> result = query.getResultList();
		return result;
	}

	public User getUser(User user) {
		Query q = em.createQuery("from User");
		List<User> res = q.getResultList();
		if (res.isEmpty()) {
			User admin = new User("admin", "admin", "0");
			em.persist(admin);
		}
		Query query = em
				.createQuery("from User u where u.username = :username and u.password = :password");
		query.setParameter("username", user.getUsername());
		query.setParameter("password", user.getPassword());
		List<User> result = query.getResultList();

		if (result.isEmpty()) {
			return new User("ERROR", "ERROR", "-1");
		}

		return result.get(0);
	}

	public User addUser(User user) {
		Query query = em.createQuery("from User");
		List<User> users = query.getResultList();
		if (!users.contains(user)) {
			em.persist(user);
			return user;
		}
		return null;
	}

	public User updateUser(User user) {
		Query query = em.createQuery("from User");
		List<User> users = query.getResultList();
		for (User u : users) {
			if (u.getLibraryPersistentID()
					.equals(user.getLibraryPersistentID())) {
				u.setPassword(user.getPassword());
				u.setUsername(user.getUsername());
				u.setLibraryPersistentID(user.getLibraryPersistentID());
				Query nameQuery = em.createQuery("select username from User");
				List<String> usernames = nameQuery.getResultList();
				int counter = 0;
				for (String s : usernames) {
					if (s.equals(user.getUsername())) {
						counter++;
					}
				}
				if (counter > 1) {
					return null;
				}
				em.merge(u);
				return u;
			}
		}
		return null;
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

	@Override
	public User getUserByLibraryPersistentID(String libraryPersistentID) {
		Query query = em
				.createQuery("from User u where u.libraryPersistentID = :libraryPersistentID");
		query.setParameter("libraryPersistentID", libraryPersistentID);
		List<User> result = query.getResultList();
		if (result.isEmpty()) {
			User tempUser = new User("TEMP", "TEMP", "-1");
			em.persist(tempUser);
			return tempUser;
		}
		return result.get(0);
	}

	@Override
	public User getUserByName(String username) {
		Query query = em
				.createQuery("from User u where u.username = :username");
		query.setParameter("username", username);
		List<User> result = query.getResultList();
		return result.get(0);
	}

}
