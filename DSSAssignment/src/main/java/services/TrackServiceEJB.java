package services;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import dao.TrackDAO;
import entities.Track;
import entities.User;

@Stateless
@Local
public class TrackServiceEJB implements TrackService{
	
	@EJB
	private TrackDAO dao;

	@Override
	public void addTracks(List<Track> tracks) {
		dao.addTracks(tracks);
		
	}

	@Override
	public Track getTrack(String trackID) {
		return dao.getTrack(trackID);
	}

	@Override
	public List<Track> getTracksByUser(String userID) {
		return dao.getTracksByUser(userID);
	}

	@Override
	public void deleteTrack(Track track) {
		dao.deleteTrack(track);
		
	}

	@Override
	public void updateTrack(Track track) {
		dao.updateTrack(track);
		
	}

	



}
