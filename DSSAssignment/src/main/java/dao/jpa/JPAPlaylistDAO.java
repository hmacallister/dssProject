package dao.jpa;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dao.PlaylistDAO;
import entities.Playlist;
import entities.User;

@Stateless
@Local
public class JPAPlaylistDAO implements PlaylistDAO{
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public void addPlaylist(Playlist playlist) {
		Query query = em.createQuery("from Playlist");
		List<Playlist> playlists = query.getResultList();
		if (!playlists.contains(playlists)) {
			em.merge(playlists);
		}
		
	}

	@Override
	public Playlist getPlaylistByTitle(String title) {
		Query query = em.createQuery("from Playlist cd where cd.title = :title");
		query.setParameter("title", title);
		List<Playlist> result = query.getResultList();

		return result.get(0);
	}

	@Override
	public Playlist getPlaylistById(int id) {
		Query query = em.createQuery("from Playlist cd where cd.id = :id");
		query.setParameter("id", id);
		List<Playlist> result = query.getResultList();

		return result.get(0);
	}

	@Override
	public void removePlaylistById(int id) {
		Playlist disc = em.find(Playlist.class, id);
		em.remove(disc);
		
	}

	@Override
	public void updatePlaylist(Playlist playlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Playlist> getPlaylistsByUser(String userID) {
		Query userQuery = em.createQuery("from User u where u.libraryPersistentID = :userID");
		userQuery.setParameter("userID", userID);
		List<User> userresult = userQuery.getResultList();
		User user = userresult.get(0);
		Query query = em.createQuery("from Playlist p where p.userFK = :user");
		query.setParameter("user", user);
		List<Playlist> result = query.getResultList();
		return result;
	}

	@Override
	public Collection<Playlist> getAllPlaylists() {
		Query query = em.createQuery("from Playlist");
		List<Playlist> result = query.getResultList();

		return result;
	}

	@Override
	public void addAllPlaylists(List<Playlist> playlists) {
		for(Playlist playlist: playlists){
			playlist.getTrackTitles();
			em.merge(playlist);
		}
		
	}

}
