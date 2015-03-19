package dao;

import java.util.Collection;

import javax.ejb.Local;

import entities.Playlist;

@Local
public interface PlaylistDAO {
	
	void addPlaylist(Playlist playlist);

	Playlist getPlaylistByTitle(String title);
	
	Playlist getPlaylistById(int id);
	
	void removePlaylistById(int id);
	
	void updatePlaylist(Playlist playlist);

	Collection<Playlist> getPlaylistsByUser(int user);

	Collection<Playlist> getAllPlaylists();

}
