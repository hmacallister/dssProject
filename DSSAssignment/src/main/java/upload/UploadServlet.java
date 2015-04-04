package upload;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import dao.PlaylistDAO;
import dao.TrackDAO;
import dao.UserDAO;


//import resources.ExcellLoader;

@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*10,	// 10MB 
				 maxFileSize=1024*1024*500,		// 500MB
				 maxRequestSize=1024*1024*500)	// 500MB
public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the directory where uploaded files will be saved, relative to
	 * the web application directory.
	 */
	private static final String SAVE_DIR = "uploadFiles";
	
	@EJB
	private PlaylistDAO playlistDao;
	@EJB
	private UserDAO userDao;
	@EJB
	private TrackDAO trackDao;
	

	//ReadLookup lookupDataReader = new ExcelLookupDataRead();
	//@Inject
	ReadXML xmlReader = new ReadXMLDataParser();
	
	/**
	 * handles file upload
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// gets absolute path of the web application
		
		
		String appPath = request.getServletContext().getRealPath("");
		// constructs path of the directory to save uploaded file
		String savePath = appPath + File.separator + SAVE_DIR;
		
		// creates the save directory if it does not exists
		File fileSaveDir = new File(savePath);
		//File myFile = new File(fileSaveDir + File.separator + "upload.txt");
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}
		
		String finalFilePath = "";
		String finalFileName = "";
	//	String fileExtension = "";
		boolean correctFileFound = false;
		Part part = request.getPart("file");
		//for (Part part : request.getParts()) {
			String fileName = extractFileName(part);
			//fileExtension = getFileExtension(fileName);
		//	if(fileExtension.equals(".xml")){
				correctFileFound = true;
				//long timeInMili = System.currentTimeMillis();
				//Date t = new Date(timeInMili);
				//timeInMili = timeInMili >> 4;
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				finalFileName = timeStamp + "_" + fileName;
				finalFilePath = savePath + File.separator + finalFileName;
				part.write(finalFilePath);
				//fileDao.addUploadedFilePath(finalFileName, finalFilePath, false);
			//}
		//}
				
		xmlReader.setInputFile(finalFilePath);
		xmlReader.setUserDao(userDao);
		xmlReader.setTrackDao(trackDao);
		xmlReader.setPlaylistDao(playlistDao);	
		//baseDataReader.setErrorBaseDataDao(errorDao);
		try {
			xmlReader.read();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		if(correctFileFound){
			String resp = "Upload Successful";
			response.sendRedirect("/dss/upload.html#"+resp);
		}
		else{
			String resp = "Upload Failed";
			response.sendRedirect("/dss/upload.html#"+resp);
		}
		
	}
	
	/*
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String finalFilePath = request.getParameter("fileSelection");
		/*
		lookupDataReader.setInputFile(finalFilePath);
		lookupDataReader.setLookUpDao(lookupDao);
		lookupDataReader.read();
		
		
		//baseDataReader.setSheetNumber(0);
		xmlReader.setInputFile(finalFilePath);
		xmlReader.setUserDao(userDao);
		xmlReader.setTrackDao(trackDao);
		xmlReader.setPlaylistDao(playlistDao);	
		//baseDataReader.setErrorBaseDataDao(errorDao);
		try {
			xmlReader.read();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//int numOfInvalidRows = baseDataReader.getInvalidRowCount();
		
		
		String resp = "Transfer to database completed successfully!";
		response.sendRedirect("/dss/upload.html#"+resp);
	}

*/

	/**
	 * Extracts file name from HTTP header content-disposition
	 */
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length()-1);
			}
		}
		return "";
	}
	
	static String getFileExtension(String fileName){
		int location = 0;
		for(int i=fileName.length()-1; i>0; i--){
			if(fileName.charAt(i)=='.'){
				location = i;
				break;
			}
		}
		return fileName.substring(location);
	}

	/*
	public void setLookupDataReader(ReadLookup lookupDataReader) {
		this.lookupDataReader = lookupDataReader;
	}
	public void setBaseDataReader(ReadBase baseDataReader) {
		this.baseDataReader = baseDataReader;
	}
	*/
}
