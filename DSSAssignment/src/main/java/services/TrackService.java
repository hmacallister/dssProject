package services;

import java.util.List;

import javax.ejb.Local;

import entities.Track;

@Local
public interface TrackService {
	
	void addTracks(List<Track> tracks);
	Track getTrack(String trackID);
	Track getTrackById(int id);
	List<Track> getTracksByUser(String userID);
	boolean deleteTrack(Track track);
	void updateTrack(Track track);
	List<Track> getTracksSearch(String searchAndId);
}
