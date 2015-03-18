package services;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Remote;

import entities.Album;

@Local
public interface AlbumService {
	 Collection<Album> getCatalog();
	 void addCDToCatalog(Album cd);
	 Album getCompactDisc(int id);
	 void deleteCompactDisc(int id);
	 void updateCompactDisc(Album cd);
	 Collection<Album> getCDsByArtist(String artist);
	 Album getCDByTitle(String title);

}
