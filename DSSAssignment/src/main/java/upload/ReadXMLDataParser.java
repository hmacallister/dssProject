package upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import dao.TrackDAO;
import dao.UserDAO;
import dao.jpa.JPAFileDAO;
import entities.Track;
import entities.User;

@Stateless
@Local
public class ReadXMLDataParser implements ReadXML {

	private String inputFile;

	private static UserDAO userDao;
	private static TrackDAO trackDAO;
	
	private static String libraryPersistentID;
	private static String title;
	private static String album;
	private static String artist;
	private static String genre;
	
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
	public void read() throws IOException, ParserConfigurationException, SAXException {
		
		//File stocks = new File("Stocks.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();

				
		log.info("root of xml file: " + doc.getDocumentElement().getNodeName());
		
		if (doc.hasChildNodes()) {	 
			printNote(doc.getChildNodes());	 
		}
		
	}
	
	 private static void printNote(NodeList nodeList) {
			
			Track track = new Track();
			List<Track> trackList = new ArrayList<Track>();
		 
		    for (int count = 0; count < nodeList.getLength(); count++) {
		    	Node tempNode = nodeList.item(count);
		    	// make sure it's element node.
		    	if (tempNode.getNodeType() == Node.ELEMENT_NODE) {		 
		    		// get node name and value
		    	//	log.info("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
		    	//	log.info("Node Value =" + tempNode.getTextContent());
		    		if(tempNode.getTextContent().equals("Library Persistent ID")){
		    			Node tempNodeLPID = nodeList.item(count+1);
		    			libraryPersistentID = tempNodeLPID.getTextContent();
		    			log.info("libraryPersistentID = " + libraryPersistentID);
		    		}
		    		if(tempNode.getTextContent().equals("Name")){
		    			Node tempNodeTitle = nodeList.item(count+1);
		    			title = tempNodeTitle.getTextContent();
		    			track.setTitle(title);
		    			log.info("title = " + title);		    			
		    		}
		    		if(tempNode.getTextContent().equals("Artist")){
		    			Node tempNodeArtist = nodeList.item(count+1);
		    			artist = tempNodeArtist.getTextContent();
		    			track.setArtist(artist);
		    			log.info("artist = " + artist);		    			
		    		}
		    		if(tempNode.getTextContent().equals("Album")){
		    			Node tempNodeAlbum = nodeList.item(count+1);
		    			album = tempNodeAlbum.getTextContent();
		    			track.setAlbum(album);
		    			log.info("album = " + album);		    			
		    		}
		    		if(tempNode.getTextContent().equals("Genre")){
		    			Node tempNodeGenre = nodeList.item(count+1);
		    			genre = tempNodeGenre.getTextContent();
		    			track.setGenre(genre);
		    			log.info("genre = " + genre);		    			
		    		}
		    		trackList.add(track);
		    			if (tempNode.hasAttributes()) {
		    				// get attributes names and values
		    				NamedNodeMap nodeMap = tempNode.getAttributes();		 
		    				for (int i = 0; i < nodeMap.getLength(); i++) {
		    					Node node = nodeMap.item(i);
		    				//	log.info("attr name : " + node.getNodeName());
		    				//	log.info("attr value : " + node.getNodeValue());		 
		    				}//end inner for
		    			}//end print node values if		 
		    			if (tempNode.hasChildNodes()) {
		    				// loop again if has child nodes
		    				printNote(tempNode.getChildNodes());
		    			}//end node has children if
		    			//log.info("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
		    	}//end outer if
		    }//end outer for
		    User user = userDao.getUserByLibraryPersistentID(libraryPersistentID);
		    track.setUser(user);
		    trackDAO.addTracks(trackList);
	 }//end print note
	


	@Override
	public void setTrackDao(TrackDAO dao) {
		this.trackDAO = dao;
	}
}//end class
