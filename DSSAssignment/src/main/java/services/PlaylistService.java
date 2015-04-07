package services;

import java.util.List;

import javax.ejb.Local;

import entities.Playlist;

@Local
public interface PlaylistService {

	void addPlaylist(Playlist playlist);

	Playlist getPlaylistByTitle(String title);

	Playlist getPlaylistById(int id);

	void removePlaylistById(int id);

	void updatePlaylist(Playlist playlist);

	List<Playlist> getPlaylistsByUser(String user);

	List<Playlist> getAllPlaylists();

}
