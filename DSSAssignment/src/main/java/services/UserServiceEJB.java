package services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import dao.UserDAO;
import entities.User;

@Stateless
@Local
public class UserServiceEJB implements UserService{
	
	@EJB
	private UserDAO dao;

	public Collection<User> getUsers() {
		return dao.getAllUsers();
	}

	
	public User getUser(User user) {
		return dao.getUser(user);
	}
	
	public User addUser(User user){
		 return dao.addUser(user);
	}
	
	public void updateUser(User user){
		 dao.updateUser(user);
	}


	public void deleteUser(User user) {
		dao.deleteUser(user);
		
	}

	public User getUserById(User user) {
		return dao.getUserById(user);
	}


	@Override
	public User getUserByLibraryPersistentID(String libraryPersistentID) {
		return dao.getUserByLibraryPersistentID(libraryPersistentID);
	}




}
