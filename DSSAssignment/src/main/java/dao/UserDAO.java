package dao;

import java.util.Collection;

import entities.User;

public interface UserDAO {
	
	Collection<User> getAllUsers();
	User getUser(User user);
	User getUserById(User user);
	User addUser(User user);
	void updateUser(User user);
	void deleteUser(User user);
}
