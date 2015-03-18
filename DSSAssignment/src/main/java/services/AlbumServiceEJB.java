package services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import dao.AlbumDAO;
import entities.Album;

@Stateless
@Local
public class AlbumServiceEJB implements AlbumService{

	@EJB
	private AlbumDAO dao;

	public void addCDToCatalog(Album cd) {
			dao.addCompactDisc(cd);
	}

	public Collection<Album> getCatalog() {
		return dao.getAllDiscs();
	}

	public Album getCompactDisc(int id) {
		return dao.getCompactDiscById(id);
	}
	
	public void deleteCompactDisc(int id) {
		dao.removeCompactDiscById(id);
	}

	public Collection<Album> getCDsByArtist(String artist) {
		return dao.getDiscsByArtist(artist);
	}

	public Album getCDByTitle(String title) {
		return dao.getCompactDiscByTitle(title);
	}

	public void updateCompactDisc(Album cd) {
		dao.updateCompactDisc(cd);
		
	}

}
