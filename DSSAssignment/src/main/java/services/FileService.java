package services;

import java.util.Collection;

import javax.ejb.Local;

import entities.FileInfo;

@Local
public interface FileService {
	Collection<FileInfo> getAllUploadedFilePaths();

	boolean addUploadedFilePath(String name, String path, boolean flush);

	void removeFileFromDatabase(String fileName);
}
