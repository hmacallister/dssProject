package dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import entities.Playlist;

@Local
public interface PlaylistDAO {

	void addPlaylist(Playlist playlist);

	void addAllPlaylists(Collection<Playlist> playlist);

	Playlist getPlaylistByTitle(String title);

	Playlist getPlaylistById(int id);

	void removePlaylistById(int id);

	void updatePlaylist(Playlist playlist);

	List<Playlist> getPlaylistsByUser(String user);

	List<Playlist> getAllPlaylists();

}
