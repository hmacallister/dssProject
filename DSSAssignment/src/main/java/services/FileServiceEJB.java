package services;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import dao.FileDAO;
import entities.FileInfo;

@Stateless
@Local
public class FileServiceEJB implements FileService{
	
	@EJB
	private FileDAO dao;

	@Override
	public Collection<FileInfo> getAllUploadedFilePaths() {
		return dao.getAllUploadedFilePaths();
	}

	@Override
	public boolean addUploadedFilePath(String name, String path, boolean flush) {
		return dao.addUploadedFilePath(name, path, flush);
	}

	@Override
	public void removeFileFromDatabase(String fileName) {
		dao.removeFileFromDatabase(fileName);
	}
}