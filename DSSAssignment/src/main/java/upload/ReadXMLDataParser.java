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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dao.PlaylistDAO;
import dao.TrackDAO;
import dao.UserDAO;
import dao.jpa.JPAFileDAO;
import entities.Playlist;
import entities.Track;
import entities.User;

@Stateless
@Local
public class ReadXMLDataParser implements ReadXML {

	private String inputFile;

	private static UserDAO userDao;
	private static TrackDAO trackDAO;
	private static PlaylistDAO playlistDAO;

	private static String libraryPersistentID;
	private static String title;
	private static String album;
	private static String artist;
	private static String genre;
	private static String trackID;
	private static String playListTitle;
	private static String playListTracks;
	private static User user = new User();
	private static List<Playlist> allPlaylists = new ArrayList<Playlist>();
	private static Map<Playlist, List<String> > playlistsMap = new HashMap<Playlist, List<String> >();
	static boolean searchTracks;
	static boolean searchPlaylists;

	private static final Logger log = LoggerFactory.getLogger(JPAFileDAO.class);

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
			printNote(doc.getChildNodes());
		}
		addPlaylists(playlistsMap);

	}

	private static void printNote(NodeList nodeList) {

		Track track = new Track();
		List<Track> trackList = new ArrayList<Track>();
		

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
					user = userDao
							.getUserByLibraryPersistentID(libraryPersistentID);
					if (user.getLibraryPersistentID().equals("-1") || user.getLibraryPersistentID().equals("TEMPORARY ID")) {
						user.setLibraryPersistentID(libraryPersistentID);
						userDao.updateUser(user);
					}
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
							playlistsMap.put(plist,plTrackList);
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
				
				
				
				if(track.getArtist() != null){
					trackList.add(track);
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
					printNote(tempNode.getChildNodes());
				}// end has children if
					// log.info("Node Name =" + tempNode.getNodeName() +
					// " [CLOSE]");
			}// end outer if
		}// end outer for
		track.setUser(user);
		
		
		
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
		
		
		
		trackDAO.addTracks(trackList);
		//playlistDAO.addAllPlaylists(allPlaylists);
	}// end print note

	public static void addPlaylists(Map<Playlist, List<String> > allPlaylists) {
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
						playlistTracks.add(trackDAO.getTrack(s));
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