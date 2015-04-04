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
public class JPATrackDAO implements TrackDAO{
	
	private final Logger log = LoggerFactory.getLogger(JPATrackDAO.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void addTracks(List<Track> tracks) {
		for(Track track: tracks){
			em.merge(track);
		}	
	}
	@Override
	public void addTrack(Track track) {
		Query query = em.createQuery("from Track");
		List<User> result = query.getResultList();
		if(!result.contains(track)){
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
		if(result.isEmpty()){
			return null;
		}
		return result.get(0);
	}

	@Override
	public List<Track> getTracksByUser(String userID) {
		Query userQuery = em.createQuery("from User u where u.libraryPersistentID = :userID");
		userQuery.setParameter("userID", userID);
		List<User> userresult = userQuery.getResultList();
		User user = userresult.get(0);
		Query query = em.createQuery("from Track t where t.user = :user");
		query.setParameter("user", user);
		List<Track> result = query.setMaxResults(100).getResultList();
		if(result.isEmpty()){
			return null;
		}
		return result;
	}
	
	@Override
	public void deleteTrack(Track track) {
		Track deletedTrack = em.find(Track.class, track.getId());
		
		List<Track> allTracks = new ArrayList<Track>();
		log.info("track being deleted is id: "+deletedTrack.getId());
		Query query = em.createQuery("from Playlist");
		List<Playlist> playlists = query.getResultList();
		for(Playlist p :playlists){
			Collection<Track> tracks = (List<Track>) p.getTrackTitles();
			for(Track t : tracks){
				if(t.getId() == track.getId()){
					try{
						em.remove(t);
						//Query deleteQuery = em.createQuery("DELETE FROM Playlist p WHERE p.trackTitles = :id");
						//int deletedCount = deleteQuery.setParameter("id", track.getId()).executeUpdate();
						
						log.info("track being deleted in playlist is id: "+t.getId() );//+ " delete count: "+deletedCount);
					}
					catch(Exception e){
						log.info("playlist couldn't remove track"+t.getId());
						//e.printStackTrace();
					}
				}
				else{
					//log.info("track not deleted in playlist is id: "+t.getId());
					t.setTitle("changed");
					if(t.getId()!= track.getId() && t !=null){
						allTracks.add(t);
						log.info("adding to allTracks track id: "+t.getId());
					}					
				}
			}
			
			/*
			try{
				p.getTrackTitles().remove(track);
				log.info("removed from playlist: "+p.getTitle());
			}
			catch(Exception e){
				log.info("couldn't remove from: "+p.getTitle());
			}
			*/
			
			//p.setTrackTitles(tracks);
			//em.merge(p);
		}
		
		try{
			em.remove(deletedTrack);
		}
		catch(Exception e){
			log.info("track couldn't remove track"+deletedTrack.getId());
		}
		//addTracks(allTracks);
	}
	
	@Override
	public void updateTrack(Track track) {

		Query query = em.createQuery("from Track");
		List<Track> tracks = query.getResultList();
		for(Track t : tracks){
			if(t.getId() == (track.getId())){
				t.setTitle(track.getTitle());
				t.setArtist(track.getArtist());
				t.setAlbum(track.getAlbum());
				t.setGenre(track.getGenre());
				em.merge(t);
				break;
			}
		}
	}

	/*
	
	public User getUser(User user) {
		Query q = em.createQuery("from User");
		List<User> res = q.getResultList();
		if(res.isEmpty()){
			User admin = new User("admin", "admin", "0");
			em.merge(admin);
		}
		Query query = em.createQuery("from User u where u.username = :username and u.password = :password");
		query.setParameter("username", user.getUsername());
		query.setParameter("password", user.getPassword());
		List<User> result = query.getResultList();
		
		if(result.isEmpty()){
			return new User("ERROR", "ERROR", "-1");
		}
		
		return result.get(0);
	}
	
	public User addUser(User user){
		Query query = em.createQuery("from User");
		List<User> users = query.getResultList();
		if (!users.contains(user)){
			em.merge(user);
			return user;
		}
		return null;
		//return "User Not Added";
	}


	public void updateUser(User user) {

		Query query = em.createQuery("from User");
		List<User> users = query.getResultList();
		for(User u : users){
			if(u.getId().equals(user.getId())){
				u.setPassword(user.getPassword());
				u.setUsername(user.getUsername());
				u.setLibraryPersistentID(user.getLibraryPersistentID());
				em.merge(u);
				break;
			}
		}
	}





	@Override
	public User getUserById(User user) {
		Query query = em.createQuery("from User u where u.id = :id");
		query.setParameter("id", user.getId());
		List<User> result = query.getResultList();
		return result.get(0);
	}


	*/
	

}
