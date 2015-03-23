package dao;

import java.util.List;

import javax.ejb.Local;

import entities.Track;

@Local
public interface TrackDAO {
	void addTracks(List<Track> tracks);
}
