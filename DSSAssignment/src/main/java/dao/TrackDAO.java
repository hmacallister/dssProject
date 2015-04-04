package dao;

import java.util.List;

import javax.ejb.Local;

import entities.Track;
import entities.User;

@Local
public interface TrackDAO {
	void addTracks(List<Track> tracks);
	Track getTrack(String trackID);
	void addTrack(Track track);
	void deleteTrack(Track track);
	void updateTrack(Track track);
	List<Track> getTracksByUser(String userID);
}
