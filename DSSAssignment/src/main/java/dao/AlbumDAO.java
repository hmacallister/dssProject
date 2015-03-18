package dao;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Remote;

import entities.Album;

@Local
public interface AlbumDAO {
	void addCompactDisc(Album disc);

	Album getCompactDiscByTitle(String title);
	
	Album getCompactDiscById(int id);
	
	void removeCompactDiscById(int id);
	
	void updateCompactDisc(Album disc);

	Collection<Album> getDiscsByArtist(String artist);

	Collection<Album> getAllDiscs();
}