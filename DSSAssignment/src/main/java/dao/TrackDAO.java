package dao;

import java.util.List;

import javax.ejb.Local;

import entities.Track;
import entities.User;

@Local
public interface TrackDAO {
	void addTracks(List<Track> tracks);
	Track getTrack(String trackID);
	Track getTrackById(int id);
	void addTrack(Track track);
	boolean deleteTrack(Track track);
	void updateTrack(Track track);
	List<Track> getTracksByUser(String userID);
	List<Track> getTracksSearch(String searchAndId);
}
