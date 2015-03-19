package dao;

import java.util.Collection;

import entities.FileInfo;

public interface FileDAO {
	
	Collection<FileInfo> getAllUploadedFilePaths();
	
	boolean addUploadedFilePath(String name, String path, boolean flush);

	void removeFileFromDatabase(String fileName);

}
