package upload;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import dao.TrackDAO;
import dao.UserDAO;

public interface ReadXML {

	void setInputFile(String inputFile);
	
	void read() throws IOException, ParserConfigurationException, SAXException;

	void setUserDao(UserDAO dao);
	void setTrackDao(TrackDAO dao);

}
