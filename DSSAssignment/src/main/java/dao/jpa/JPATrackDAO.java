package dao.jpa;

import java.util.ArrayList;
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
import entities.Playlist;
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
			em.merge(track);
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
	public void deleteTrack(Track track) {
		Track deletedTrack = em.find(Track.class, track.getId());

		List<Track> allTracks = new ArrayList<Track>();
		log.info("***** track about to be deleted is id: " + deletedTrack.getId());
		
		/*
		 * Query query = em.createQuery("from Playlist"); List<Playlist>
		 * playlists = query.getResultList(); for(Playlist p :playlists){
		 * Collection<Track> tracks = (List<Track>) p.getTrackTitles();
		 * for(Track t : tracks){ if(t.getId() == track.getId()){ try{
		 * em.remove(t); //Query deleteQuery =
		 * em.createQuery("DELETE FROM Playlist p WHERE p.trackTitles = :id");
		 * //int deletedCount = deleteQuery.setParameter("id",
		 * track.getId()).executeUpdate();
		 * 
		 * log.info("track being deleted in playlist is id: "+t.getId() );//+
		 * " delete count: "+deletedCount); } catch(Exception e){
		 * log.info("playlist couldn't remove track"+t.getId());
		 * //e.printStackTrace(); } } else{
		 * //log.info("track not deleted in playlist is id: "+t.getId());
		 * t.setTitle("changed"); if(t.getId()!= track.getId() && t !=null){
		 * allTracks.add(t);
		 * log.info("adding to allTracks track id: "+t.getId()); } } }
		 * 
		 * /* try{ p.getTrackTitles().remove(track);
		 * log.info("removed from playlist: "+p.getTitle()); } catch(Exception
		 * e){ log.info("couldn't remove from: "+p.getTitle()); }
		 */

		// p.setTrackTitles(tracks);
		// em.merge(p);
		// }

		try {
			//Query deleteQuery = em.createQuery("DELETE FROM Track t WHERE t.id = :id");
			//int deletedCount = deleteQuery.setParameter("id", track.getId()).executeUpdate();
			//log.info("***** track being deleted in playlist is id: "+track.getId() + " deleted tracks count: "+deletedCount);
			em.remove(deletedTrack);
		} catch (Exception e) {
			log.info("couldn't remove track" + deletedTrack.getId());
			//e.printStackTrace();
		}
		// addTracks(allTracks);
	}

	@Override
	public void updateTrack(Track track) {

		Query query = em.createQuery("from Track");
		List<Track> tracks = query.getResultList();
		log.info("editing: "+track.getId());
		for (Track t : tracks) {
			//log.info("track list id: "+t.getId());
			if (t.getId() == track.getId()) {
				t.setTitle(track.getTitle());
				t.setArtist(track.getArtist());
				t.setAlbum(track.getAlbum());
				t.setGenre(track.getGenre());
				log.info("edited title: "+track.getTitle());
				em.merge(t);
				break;
			}
		}
	}

	@Override
	public List<Track> getTracksSearch(String searchAndId) {
		String[] split = searchAndId.split("--user--");
		String userID = split[1];
		String searchTerm = split[0].toLowerCase();
		List<Track> searchResults = new ArrayList<Track>();
		log.info("******** get tracks user id is: " + userID
				+ " searchTerm is: " + searchTerm);
		try {
			Query userQuery = em
					.createQuery("from User u where u.libraryPersistentID = :userID");
			userQuery.setParameter("userID", userID);
			List<User> userresult = userQuery.getResultList();
			User user = userresult.get(0);
			Query query = em.createQuery("from Track t where t.user = :user");
			query.setParameter("user", user);
			List<Track> result = query.getResultList();
			for (Track track : result) {
				if (track.getTitle().toLowerCase().contains(searchTerm)) {
					searchResults.add(track);
				} else if (track.getArtist().toLowerCase().contains(searchTerm)) {
					searchResults.add(track);
				} else if (track.getAlbum().toLowerCase().contains(searchTerm)) {
					searchResults.add(track);
				} else if (track.getGenre().toLowerCase().contains(searchTerm)) {
					searchResults.add(track);
				}
			}

			if (result.isEmpty() || searchResults.isEmpty()) {
				return null;
			}
			return searchResults;
		} catch (Exception e) {
			log.info("search exception");
		}
		return null;
	}

}
