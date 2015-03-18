package dao.jpa;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dao.AlbumDAO;
import entities.Album;

@Stateless
@Local
public class JPAAlbumDAO implements AlbumDAO {

	@PersistenceContext
	private EntityManager em;

	public void addCompactDisc(Album disc) {

		Query query = em.createQuery("from Album");
		List<Album> discs = query.getResultList();
		if (!discs.contains(disc)) {

			em.persist(disc);
		}

	}

	public Album getCompactDiscByTitle(String title) {

		Query query = em
				.createQuery("from Album cd where cd.title = :title");
		query.setParameter("title", title);
		List<Album> result = query.getResultList();

		return result.get(0);

	}

	public Collection<Album> getDiscsByArtist(String artist) {

		Query query = em
				.createQuery("from Album cd where cd.artist = :artist");
		query.setParameter("artist", artist);
		List<Album> result = query.getResultList();

		return result;

	}

	public Collection<Album> getAllDiscs() {

		Query query = em.createQuery("from Album");
		List<Album> result = query.getResultList();

		return result;
	}

	public Album getCompactDiscById(int id) {

		Query query = em.createQuery("from Album cd where cd.id = :id");
		query.setParameter("id", id);
		List<Album> result = query.getResultList();

		return result.get(0);
	}

	public void removeCompactDiscById(int id) {

		Album disc = em.find(Album.class, id);
		em.remove(disc);
		/*
		Query query = em
				.createQuery("delete * from Album cd where cd.id = :id");
		query.setParameter("id", id);
		List<Album> result = query.getResultList();
		*/

	}

	public void updateCompactDisc(Album disc) {
		
		Album cd = em.find(Album.class, disc.getId());
		cd.setArtist(disc.getArtist());
		cd.setGenre(disc.getGenre());
		cd.setTitle(disc.getTitle());
		cd.setTracks(disc.getTracks());
		
		/*
		Query query = em
				.createQuery("update Album cd SET disc  where cd.id = :id");
		query.setParameter("id", disc.getId()).executeUpdate();
		*/
	}

}
