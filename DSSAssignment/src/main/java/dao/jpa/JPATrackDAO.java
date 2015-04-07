package dao.jpa;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.TrackDAO;
import entities.Track;
import entities.User;

@Stateless
@Local
public class JPATrackDAO implements TrackDAO {

	private final Logger log = LoggerFactory.getLogger(JPATrackDAO.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public void addTracks(List<Track> tracks) {
		for (Track track : tracks) {
			Query query = em.createQuery("from Track");
			List<User> result = query.getResultList();
			if (!result.contains(track)) {
				em.merge(track);
			}
		}
	}

	@Override
	public void addTrack(Track track) {
		Query query = em.createQuery("from Track");
		List<User> result = query.getResultList();
		if (!result.contains(track)) {
			em.merge(track);
		}
	}

	public Collection<User> getAllTracks() {
		Query query = em.createQuery("from Track");
		List<User> result = query.getResultList();
		return result;
	}

	@Override
	public Track getTrack(String trackId) {
		Query query = em.createQuery("from Track t where t.trackId = :trackId");
		query.setParameter("trackId", trackId);
		List<Track> result = query.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public List<Track> getTracksByUser(String userID) {
		Query userQuery = em
				.createQuery("from User u where u.libraryPersistentID = :userID");
		userQuery.setParameter("userID", userID);
		List<User> userresult = userQuery.getResultList();
		User user = userresult.get(0);
		Query query = em.createQuery("from Track t where t.user = :user");
		query.setParameter("user", user);
		List<Track> result = query.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	@Override
	public boolean deleteTrack(Track track) {
		Query deleteQuery = em.createQuery("FROM Track t WHERE t.id = :id");
		deleteQuery.setParameter("id", track.getId());
		List<Track> tracks = deleteQuery.getResultList();
		Track deletedTrack = tracks.get(0);
		try {
			em.remove(deletedTrack);
			return true;
		} catch (Exception e) {

		}
		return false;

	}

	@Override
	public void updateTrack(Track track) {
		Track updateTrack = em.find(Track.class, track.getId());
		updateTrack.setTitle(track.getTitle());
		updateTrack.setArtist(track.getArtist());
		updateTrack.setAlbum(track.getAlbum());
		updateTrack.setGenre(track.getGenre());
		log.info("edited title: " + track.getTitle());
		em.merge(updateTrack);

	}

	@Override
	public Track getTrackById(int id) {
		Query query = em.createQuery("from Track t where t.id = :id");
		query.setParameter("id", id);
		List<Track> result = query.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

}
