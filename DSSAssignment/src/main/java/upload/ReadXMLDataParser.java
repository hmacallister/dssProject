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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dao.UserDAO;
import dao.jpa.JPAFileDAO;

@Stateless
@Local
public class ReadXMLDataParser implements ReadXML {

	private String inputFile;

	private UserDAO userDao;
	
	private String libraryPersistentID;
	private String title;
	private String album;
	private String artist;
	private String genre;
	
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
			*/
			
			

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



}
