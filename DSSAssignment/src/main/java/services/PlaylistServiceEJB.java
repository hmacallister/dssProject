package services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import dao.PlaylistDAO;
import entities.Playlist;

@Stateless
@Local
public class PlaylistServiceEJB implements PlaylistService{
	
	@EJB
	private PlaylistDAO dao;

	@Override
	public void addPlaylist(Playlist playlist) {
		dao.addPlaylist(playlist);
		
	}

	@Override
	public Playlist getPlaylistByTitle(String title) {
		return dao.getPlaylistByTitle(title);
	}

	@Override
	public Playlist getPlaylistById(int id) {
		return dao.getPlaylistById(id);
	}

	@Override
	public void removePlaylistById(int id) {
		dao.removePlaylistById(id);
		
	}

	@Override
	public void updatePlaylist(Playlist playlist) {
		dao.updatePlaylist(playlist);
		
	}

	@Override
	public Collection<Playlist> getPlaylistsByUser(String user) {
		return dao.getPlaylistsByUser(user);
	}

	@Override
	public Collection<Playlist> getAllPlaylists() {
		return dao.getAllPlaylists();
	}

}
