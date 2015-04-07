package dao.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.PlaylistDAO;
import entities.Playlist;
import entities.Track;
import entities.User;

@Stateless
@Local
public class JPAPlaylistDAO implements PlaylistDAO {

	@PersistenceContext
	private EntityManager em;

	private final Logger log = LoggerFactory.getLogger(JPAPlaylistDAO.class);

	@Override
	public void addPlaylist(Playlist playlist) {
		Query query = em.createQuery("from Playlist");
		List<Playlist> playlists = query.getResultList();
		if (!playlists.contains(playlist)) {
			em.merge(playlist);
		}

	}

	@Override
	public Playlist getPlaylistByTitle(String title) {
		Query query = em
				.createQuery("from Playlist cd where cd.title = :title");
		query.setParameter("title", title);
		List<Playlist> result = query.getResultList();
		if (result.isEmpty()) {
			Playlist t = new Playlist("empty");
			return t;
		}
		return result.get(0);
	}

	@Override
	public Playlist getPlaylistById(int id) {
		Query query = em.createQuery("from Playlist cd where cd.id = :id");
		query.setParameter("id", id);
		List<Playlist> result = query.getResultList();
		if (result.isEmpty()) {
			Playlist t = new Playlist("empty");
			return t;
		}
		Playlist playlist = result.get(0);
		List<Track> tracks = playlist.getTrackTitles();
		List<String> ids = new ArrayList<String>();
		for (Track t : tracks) {
			ids.add(t.getTrackId());
		}
		Collections.sort(ids);
		List<Track> orderedTracks = new ArrayList<Track>();
		for (int i = 0; i < ids.size(); i++) {
			for (Track t : tracks) {
				if (t.getTrackId().equals(ids.get(i))) {
					orderedTracks.add(t);
				}
			}
		}
		playlist.setTrackTitles(orderedTracks);
		return playlist;
	}

	@Override
	public void removePlaylistById(int id) {
		Playlist disc = em.find(Playlist.class, id);
		em.remove(disc);

	}

	@Override
	public void updatePlaylist(Playlist playlist) {
		log.info("playlist in dao: " + playlist.getTitle());
		Query query = em.createQuery("from Playlist");
		List<Playlist> playlists = query.getResultList();
		List<Track> playlistTracks = new ArrayList<Track>();
		List<String> trackIds = new ArrayList<String>();
		for (Track t : playlist.getTrackTitles()) {
			Query trackQuery = em
					.createQuery("from Track t where t.id = :trackId");
			trackQuery.setParameter("trackId", t.getId());
			List<Track> result = trackQuery.getResultList();
			Track track = result.get(0);
			trackIds.add(track.getTrackId());
			playlistTracks.add(track);
		}

		Collections.sort(trackIds);
		for (int i = 0; i < trackIds.size(); i++) {
			playlistTracks.get(i).setTrackId(trackIds.get(i));
		}
		for (Playlist p : playlists) {
			if (p.getId() == playlist.getId()) {
				p.setTrackTitles(playlistTracks);
				em.persist(p);
				break;
			}
		}

	}

	@Override
	public List<Playlist> getPlaylistsByUser(String userID) {
		Query userQuery = em
				.createQuery("from User u where u.libraryPersistentID = :userID");
		userQuery.setParameter("userID", userID);
		List<User> userresult = userQuery.getResultList();
		User user = userresult.get(0);
		Query query = em
				.createQuery("from Playlist p where p.userFK = :user order by title");
		query.setParameter("user", user);
		List<Playlist> result = query.getResultList();
		return result;
	}

	@Override
	public List<Playlist> getAllPlaylists() {
		Query query = em.createQuery("from Playlist");
		List<Playlist> result = query.getResultList();

		return result;
	}

	@Override
	public void addAllPlaylists(Collection<Playlist> playlists) {
		for (Playlist playlist : playlists) {
			Query query = em.createQuery("from Playlist");
			List<Playlist> existingPlaylists = query.getResultList();
			if (!existingPlaylists.contains(playlist)) {
				playlist.getTrackTitles();
				em.merge(playlist);
			} else {
				log.info("merging update");
				playlist.getTrackTitles();
				updatePlaylist(playlist);
			}
		}

	}

}
