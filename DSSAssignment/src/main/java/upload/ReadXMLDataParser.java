package upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dao.PlaylistDAO;
import dao.TrackDAO;
import dao.UserDAO;
import entities.Playlist;
import entities.Track;
import entities.User;

@Stateless
@Local
public class ReadXMLDataParser implements ReadXML {

	private String inputFile;
	private UserDAO userDao;
	private TrackDAO trackDAO;
	private PlaylistDAO playlistDAO;

	private String libraryPersistentID;
	private String title;
	private String album;
	private String artist;
	private String genre;
	private String trackID;
	private String playListTitle;
	private String playListTracks;
	private User user = new User();
	private List<Track> trackList = new ArrayList<Track>();
	private Map<Playlist, List<String>> playlistsMap = new HashMap<Playlist, List<String>>();
	private boolean searchTracks;
	private boolean searchPlaylists;

	private final Logger log = LoggerFactory.getLogger(ReadXMLDataParser.class);

	public ReadXMLDataParser() {
	}

	@Override
	public void read() throws IOException, ParserConfigurationException,
			SAXException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		searchTracks = false;
		searchPlaylists = false;

		log.info("root of xml file: " + doc.getDocumentElement().getNodeName());

		if (doc.hasChildNodes()) {
			addData(doc.getChildNodes());
		}
		addPlaylists(playlistsMap);

	}

	private void addData(NodeList nodeList) {

		Track track = new Track();

		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);
			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.getTextContent().equals("Tracks")) {
					log.info("Node Value =" + tempNode.getTextContent());
					searchTracks = true;
					searchPlaylists = false;
				}
				if (tempNode.getTextContent().equals("Playlists")) {
					log.info("Node Value =" + tempNode.getTextContent());
					searchTracks = false;
					searchPlaylists = true;
				}
				if (tempNode.getTextContent().equals("Library Persistent ID")) {
					Node tempNodeLPID = nodeList.item(count + 1);
					libraryPersistentID = tempNodeLPID.getTextContent();
					user = userDao
							.getUserByLibraryPersistentID(libraryPersistentID);
					if (user.getLibraryPersistentID().equals("-1")) {
						user = userDao
								.getUserByLibraryPersistentID("TEMPORARY ID");
					}
					log.info("user returned from temp is: "
							+ user.getUsername() + " user lib id is now: "
							+ libraryPersistentID);
					user.setLibraryPersistentID(libraryPersistentID);
					userDao.updateUser(user);
				}
				if (searchTracks == true) {
					if (tempNode.getTextContent().equals("Track ID")) {
						Node tempNodeTitle = nodeList.item(count + 1);
						trackID = tempNodeTitle.getTextContent();
						track.setTrackId(trackID);
					}
					if (tempNode.getTextContent().equals("Name")) {
						Node tempNodeTitle = nodeList.item(count + 1);
						title = tempNodeTitle.getTextContent();
						track.setTitle(title);
					}
					if (tempNode.getTextContent().equals("Artist")) {
						Node tempNodeArtist = nodeList.item(count + 1);
						artist = tempNodeArtist.getTextContent();
						track.setArtist(artist);
					}
					if (tempNode.getTextContent().equals("Album")) {
						Node tempNodeAlbum = nodeList.item(count + 1);
						album = tempNodeAlbum.getTextContent();
						track.setAlbum(album);
					}
					if (tempNode.getTextContent().equals("Genre")) {
						Node tempNodeGenre = nodeList.item(count + 1);
						genre = tempNodeGenre.getTextContent();
						track.setGenre(genre);
					}
				}
				if (searchPlaylists == true) {
					String playlistInfo = tempNode.getTextContent();
					if (playlistInfo.contains("ItemsPlaylist")) {
						try {
							playlistInfo = playlistInfo.substring(4);
							String[] splitTitle = playlistInfo
									.split("Playlist ID");
							playListTitle = splitTitle[0];
							String[] splitTracks = playlistInfo
									.split("All ItemsPlaylist Items");
							playListTracks = splitTracks[1];
						} catch (Exception e) {
							log.info("exception in playlist parse!");
							playListTitle = null;
							playListTracks = null;
						}

					}

					if (playListTitle != null && !playListTitle.equals("")) {
						if (playListTracks != null && user != null) {
							Playlist plist = new Playlist(playListTitle);
							ArrayList<String> plTrackList = new ArrayList<String>();
							String[] splitTracks = playListTracks
									.split("Track ID");
							for (int i = 0; i < splitTracks.length; i++) {
								plTrackList.add(splitTracks[i]);
							}
							if (!playlistsMap.containsKey(plist)) {
								playlistsMap.put(plist, plTrackList);
							}

						}
					}
				}
				if (user != null) {
					track.setUser(user);
					if (track.getArtist() != null && !trackList.contains(track)) {
						trackList.add(track);
					}
				}

				if (tempNode.hasChildNodes()) {
					// loop again if has child nodes
					addData(tempNode.getChildNodes());
				}// end has children if
			}// end outer if
		}// end outer for

	}// end print note

	public void addPlaylists(Map<Playlist, List<String>> allPlaylists) {

		try {
			trackDAO.addTracks(trackList);
		} catch (Exception e) {
			log.info("can't add tracks ");
		}
		List<Playlist> allPlaylistsData = new ArrayList<Playlist>();
		for (Entry<Playlist, List<String>> entry : allPlaylists.entrySet()) {
			try {
				Playlist playlist = entry.getKey();
				playlist.setUserFK(user);
				List<String> playlistTrackIDs = entry.getValue();
				List<Track> playlistTracks = new ArrayList<Track>();
				for (String s : playlistTrackIDs) {
					try {
						Track track = trackDAO.getTrack(s);
						if (track != null) {
							track.setUser(user);
							if (!playlistTracks.contains(track)) {
								playlistTracks.add(track);
							}
						}
					} catch (Exception e) {
						log.info("no record of this track" + s);
					}
				}
				playlist.setTrackTitles(playlistTracks);
				allPlaylistsData.add(playlist);
			} catch (Exception e) {
				log.info("error in the playlist: " + entry.getKey().getTitle());
			}
		}
		playlistDAO.addAllPlaylists(allPlaylistsData);
		user = new User();
		trackList = new ArrayList<Track>();
		playlistsMap = new HashMap<Playlist, List<String>>();

	}

	@Override
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	@Override
	public void setUserDao(UserDAO dao) {
		this.userDao = dao;
	}

	@Override
	public void setTrackDao(TrackDAO dao) {
		this.trackDAO = dao;
	}

	@Override
	public void setPlaylistDao(PlaylistDAO dao) {
		this.playlistDAO = dao;
	}
}// end class