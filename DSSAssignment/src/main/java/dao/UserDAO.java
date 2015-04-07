package dao;

import java.util.Collection;

import entities.User;

public interface UserDAO {

	Collection<User> getAllUsers();

	User getUser(User user);

	User getUserById(User user);

	User getUserByName(String username);

	User getUserByLibraryPersistentID(String libraryPersistentID);

	User addUser(User user);

	User updateUser(User user);

	void deleteUser(User user);
}
