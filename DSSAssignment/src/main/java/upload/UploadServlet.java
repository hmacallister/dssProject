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

@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
maxFileSize = 1024 * 1024 * 500, // 500MB
maxRequestSize = 1024 * 1024 * 500)
// 500MB
public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String SAVE_DIR = "uploadFiles";

	@EJB
	private PlaylistDAO playlistDao;
	@EJB
	private UserDAO userDao;
	@EJB
	private TrackDAO trackDao;

	ReadXML xmlReader = new ReadXMLDataParser();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String appPath = request.getServletContext().getRealPath("");
		String savePath = appPath + File.separator + SAVE_DIR;
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

		String finalFilePath = "";
		String finalFileName = "";
		boolean correctFileFound = false;
		Part part = request.getPart("file");

		String fileName = extractFileName(part);

		correctFileFound = true;

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		finalFileName = timeStamp + "_" + fileName;
		finalFilePath = savePath + File.separator + finalFileName;
		part.write(finalFilePath);

		xmlReader.setInputFile(finalFilePath);
		xmlReader.setUserDao(userDao);
		xmlReader.setTrackDao(trackDao);
		xmlReader.setPlaylistDao(playlistDao);

		try {
			xmlReader.read();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		if (correctFileFound) {
			String resp = "Upload Successful";
			response.sendRedirect("/dss/upload.html#" + resp);
		} else {
			String resp = "Upload Failed";
			response.sendRedirect("/dss/upload.html#" + resp);
		}

	}

	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}

	static String getFileExtension(String fileName) {
		int location = 0;
		for (int i = fileName.length() - 1; i > 0; i--) {
			if (fileName.charAt(i) == '.') {
				location = i;
				break;
			}
		}
		return fileName.substring(location);
	}
}
