package upload;

import java.io.IOException;
import java.util.ArrayList;

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

@Stateless
@Local
public class ReadXMLDataParser implements ReadXML {

	private String inputFile;

	private UserDAO userDao;
	private static TrackDAO trackDAO;
	
	private static String libraryPersistentID;
	private static String title;
	private static String album;
	private static String artist;
	private static String genre;
	private Track track;
	private static ArrayList<Track> trackList;
	
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
		
		
		/*
		
		NodeList nodes = doc.getElementsByTagName("dict");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			log.info("PARENT NODE IS: "+ node.getNodeName());
			/*
			
			Element element1 = (Element) node;
			NodeList childNodes = element1.getElementsByTagName("key");
			
			 * for(int j= 0; j < childNodes.getLength(); j++){
				Node node2 = childNodes.item(i);
				String childName = node2.getNodeName();
				log.info("CHILD NODE IS: "+ childName);
				Element element5 = (Element) node2;
				String value = getValue(childName, element5);
				log.info("VALUE IS: "+ value);
			}
			
			
			

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				ArrayList<String> keyValues = getValue("key", element);
				for(String s:keyValues){
					log.info("Value IS: "+s);
				}
				//log.info("KEY IS: "+key);								
				//System.out.println("Stock Price: " + getValue("price", element));
				//System.out.println("Stock Quantity: " + getValue("quantity", element));
			}
			
			
		}		
	}
	private static ArrayList<String> getValue(String tag, Element element) {
		ArrayList<String> values = new ArrayList<String>();
		values.add("VALUES START HERE");
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		List<Node> nodeList = new ArrayList<Node>();
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = (Node) element.getElementsByTagName(tag).item(i).getChildNodes();
			//values.add(element.getElementsByTagName(tag).item(i).getChildNodes());
			nodeList.add(node);
			//values.add(node.getNodeValue());
		}
		
		for(Node n: nodeList){
			
			//Node node = (Node) nodes.item(i);
			values.add(n.getNodeValue());
		}
		//Node node = (Node) nodes.item(0);
	
		return values;// node.getNodeValue();
	}

	*/
	}
	
	 private static void printNote(NodeList nodeList) {
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
		    			log.info("title = " + title);		    			
		    		}
		    		if(tempNode.getTextContent().equals("Artist")){
		    			Node tempNodeArtist = nodeList.item(count+1);
		    			artist = tempNodeArtist.getTextContent();
		    			log.info("artist = " + artist);		    			
		    		}
		    		if(tempNode.getTextContent().equals("Album")){
		    			Node tempNodeAlbum = nodeList.item(count+1);
		    			album = tempNodeAlbum.getTextContent();
		    			log.info("album = " + album);		    			
		    		}
		    		if(tempNode.getTextContent().equals("Genre")){
		    			Node tempNodeGenre = nodeList.item(count+1);
		    			genre = tempNodeGenre.getTextContent();
		    			log.info("genre = " + genre);		    			
		    		}
		    		trackList.add(new Track(title,album,artist,genre));
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
		    trackDAO.addTracks(trackList);
	 }//end print note
	
	 
	 public void setTrackDAO(TrackDAO dao) {
			this.trackDAO = dao;
	}
}//end class
