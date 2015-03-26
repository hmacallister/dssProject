package services;

import java.util.Collection;

import javax.ejb.Local;

import entities.User;

@Local
public interface UserService {
	
	Collection<User> getUsers();
	User getUser(User user);
	User getUserById(User user);
	User addUser(User user);
	User getUserByLibraryPersistentID(String libraryPersistentID);
	void updateUser(User user);
	void deleteUser(User user);
	String getUserByName(String username);

}
