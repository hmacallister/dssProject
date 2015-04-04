package upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.w3c.dom.NamedNodeMap;
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
	//private static List<Playlist> allPlaylists = new ArrayList<Playlist>();
	private Map<Playlist, List<String> > playlistsMap = new HashMap<Playlist, List<String> >();
	private boolean searchTracks;
	private boolean searchPlaylists;

	private final Logger log = LoggerFactory.getLogger(ReadXMLDataParser.class);

	public ReadXMLDataParser() {
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
	public void read() throws IOException, ParserConfigurationException,
			SAXException {

		// File stocks = new File("Stocks.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		searchTracks=false;
		searchPlaylists=false;

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
			Playlist playlist = new Playlist();
			List<String> playlistTrackList = new ArrayList<String>();
			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				// get node name and value
				// log.info("\nNode Name =" + tempNode.getNodeName() +
				// " [OPEN]");
				// log.info("Node Value =" + tempNode.getTextContent());
				if (tempNode.getTextContent().equals("Tracks")){
					log.info("Node Value =" + tempNode.getTextContent());
					searchTracks = true;
					searchPlaylists = false;
				}
				if (tempNode.getTextContent().equals("Playlists")){
					log.info("Node Value =" + tempNode.getTextContent());
					searchTracks = false;
					searchPlaylists = true;
				}
				if (tempNode.getTextContent().equals("Library Persistent ID")) {
					Node tempNodeLPID = nodeList.item(count + 1);
					libraryPersistentID = tempNodeLPID.getTextContent();
//					user = userDao
//							.getUserByLibraryPersistentID(libraryPersistentID);
//					if (user.getLibraryPersistentID().equals("-1") || user.getLibraryPersistentID().equals("TEMPORARY ID")) {
//						user.setLibraryPersistentID(libraryPersistentID);
//						userDao.updateUser(user);
//					}
					user = userDao.getUserByLibraryPersistentID("TEMPORARY ID");
					log.info("user returned from temp is: "+user.getUsername()+" user lib id is now: "+libraryPersistentID);
					user.setLibraryPersistentID(libraryPersistentID);
					userDao.updateUser(user);
				}
				if (searchTracks == true) {
					if (tempNode.getTextContent().equals("Track ID")) {
						Node tempNodeTitle = nodeList.item(count + 1);
						trackID = tempNodeTitle.getTextContent();
						track.setTrackId(trackID);
						// log.info("title = " + title);
					}
					if (tempNode.getTextContent().equals("Name")) {
						Node tempNodeTitle = nodeList.item(count + 1);
						title = tempNodeTitle.getTextContent();
						track.setTitle(title);
						// log.info("title = " + title);
					}
					if (tempNode.getTextContent().equals("Artist")) {
						Node tempNodeArtist = nodeList.item(count + 1);
						artist = tempNodeArtist.getTextContent();
						track.setArtist(artist);
						// log.info("artist = " + artist);
					}
					if (tempNode.getTextContent().equals("Album")) {
						Node tempNodeAlbum = nodeList.item(count + 1);
						album = tempNodeAlbum.getTextContent();
						track.setAlbum(album);
						// log.info("album = " + album);
					}
					if (tempNode.getTextContent().equals("Genre")) {
						Node tempNodeGenre = nodeList.item(count + 1);
						genre = tempNodeGenre.getTextContent();
						track.setGenre(genre);
						// log.info("genre = " + genre);
					}
				}
				if (searchPlaylists == true) {
					//log.info("while playlists true temp noe is: "+ nodeList.item(count).getTextContent());
					if (tempNode.getTextContent().equals("Playlist ID")) {
						// Node tempNodePlaylistName = nodeList.item(count-1);
						playListTitle = nodeList.item(count - 2)
								.getTextContent();
						// log.info("playlist title:  = " + playListTitle);
						playlist.setTitle(playListTitle);
					}
					if (tempNode.getTextContent().equals("Playlist Items")) {
						// Node tempNodePlaylistTracks = nodeList.item(count+1);
						playListTracks = nodeList.item(count + 2)
								.getTextContent();
						// log.info("all tracks on playlist: "+playListTracks);
						String[] splitTracks = playListTracks.split("Track ID");
						for (int i = 0; i < splitTracks.length; i++) {
							playlistTrackList.add(splitTracks[i]);
							 //log.info("playlist track: "+splitTracks[i]);
						}
					}
					if(playListTitle != null && !playListTitle.equals("")){
						if(playListTracks !=null && user != null){
							Playlist plist = new Playlist(playListTitle);
							ArrayList<String> plTrackList = new ArrayList<String>();
							String[] splitTracks = playListTracks.split("Track ID");
							for (int i = 0; i < splitTracks.length; i++) {
								plTrackList.add(splitTracks[i]);
							}
							if(!playlistsMap.containsKey(plist)){
								playlistsMap.put(plist,plTrackList);
							}
							
						}
					}
					//log.info("playlists title: "+ playListTitle+ " trascks = "+playListTracks);
				}
				//playlist.setUserFK(user);
				//playlistsMap.put(playlist,playlistTrackList);
				//playlist.setTrackIDs(playlistTrackList);
				
				
//				
//				if(playlist.getTitle() != null){
//					for(String s: playlistTrackList){
//					//	log.info("track on add to lists are "+s);
//					}
//					allPlaylists.add(playlist);
//					//playlistsMap.put(playlist,playlistTrackList);
//				}
				if(user != null){
					track.setUser(user);
					if(track.getArtist() != null && !trackList.contains(track)){
						trackList.add(track);
					}
				}

				if (tempNode.hasAttributes()) {
					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();
					for (int i = 0; i < nodeMap.getLength(); i++) {
						Node node = nodeMap.item(i);
						// log.info("attr name : " + node.getNodeName());
						// log.info("attr value : " + node.getNodeValue());
					}// end inner for
				}// end print node values if
				if (tempNode.hasChildNodes()) {
					// loop again if has child nodes
					addData(tempNode.getChildNodes());
				}// end has children if
					// log.info("Node Name =" + tempNode.getNodeName() +
					// " [CLOSE]");
			}// end outer if
		}// end outer for
		
		
		
		/*
		for(Track t:trackList){
			//log.info("Track: "+t.getTitle());
		}
		
		*/
		
		
		
	
//		for(Entry<Playlist, List<String>> entry : playlistsMap.entrySet()){
//			Playlist key = entry.getKey();
//			List<String> values = entry.getValue();
//			log.info("for playlist: "+key.getTitle()+" the track ids are: ");
//			for(String s: values){
//				log.info("track "+s);
//			}
//			
//			
//		}
		
		
		
		/*
		for(Playlist p: allPlaylists){
			List<String> t = p.getTrackIDs();
			if(t.isEmpty()){
				//log.info("no track ids now in playlist: "+p.getTitle());
			}
			
			for(String s: t){
				//log.info("tracks in playlist is after added to allPLaylists are: : "+s);
			}
			//log.info("Playlist: "+p.getTitle() + " tracks ids size is : "+t.size());
		}
		*/
		
		
		
		
		//playlistDAO.addAllPlaylists(allPlaylists);
	}// end print note

	public void addPlaylists(Map<Playlist, List<String> > allPlaylists) {
		//trackDAO.addTracks(trackList);
		for(Track t:trackList){
			try{
				trackDAO.addTrack(t);
			}
			catch(Exception e){
				log.info("can't add track " + t.getTitle());
			}
		}
		List<Playlist> allPlaylistsData = new ArrayList<Playlist>();
		for(Entry<Playlist, List<String>> entry : allPlaylists.entrySet()){
			try{
				Playlist playlist = entry.getKey();
				playlist.setUserFK(user);
				List<String> playlistTrackIDs = entry.getValue();
				List<Track> playlistTracks = new ArrayList<Track>();
				//log.info("for playlist: "+playlist.getTitle()+" the track ids are: ");
				for(String s: playlistTrackIDs){
					try{
						Track track = trackDAO.getTrack(s);
						if(track != null){
							track.setUser(user);
							if(!playlistTracks.contains(track)){
								playlistTracks.add(track);
							}	
						}
					}	
					catch(Exception e){
						log.info("no record of this track" + s);
					}
					//log.info("track "+s);
				}
				playlist.setTrackTitles(playlistTracks);
				allPlaylistsData.add(playlist);				
			}
			catch(Exception e){
				log.info("error in the playlist: "+ entry.getKey().getTitle());
			}
		}
		playlistDAO.addAllPlaylists(allPlaylistsData);
		user = new User();
		trackList = new ArrayList<Track>();
		playlistsMap = new HashMap<Playlist, List<String> >();
		
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