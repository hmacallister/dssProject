package services;

import java.util.Collection;

import javax.ejb.Local;

import entities.Playlist;
import entities.Track;

@Local
public interface PlaylistService {
	
	void addPlaylist(Playlist playlist);

	Playlist getPlaylistByTitle(String title);
	
	Playlist getPlaylistById(int id);
	
	void removePlaylistById(int id);
	
	void updatePlaylist(Playlist playlist);
	
	void deleteTrackFromPlaylist(Track track);

	Collection<Playlist> getPlaylistsByUser(String user);

	Collection<Playlist> getAllPlaylists();

}
