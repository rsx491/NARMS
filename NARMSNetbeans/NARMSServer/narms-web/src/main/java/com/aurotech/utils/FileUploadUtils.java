package com.aurotech.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.aurotech.constants.NarmsConstants;
import com.aurotech.controller.rest.NarmsSampleController;
import com.aurotech.init.AppContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUploadUtils {

private static final Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);	
    
	/**
	 * This method will create file under given parent directory with provided file name.
	 * <br />
	 * If parent directory structure not exist it will create it.
	 * <br />
	 * If file already exist, it will be delete old file and create new blank file.
	 * 
	 * @param relativeDirPath - relative path to parent directory, If null file will be created under root directory
	 * @param fileName - File Name for file to create
	 * @return - Created file
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static File createFileInRepo(String relativeDirPath, String fileName) 
			throws IOException, URISyntaxException {
		Assert.hasText(fileName, "File Name is required.");
		Assert.isTrue(FilenameUtils.indexOfExtension(fileName) > 0 , 
				"Invalid File Name. File name with extention is required.");
		
		// Create directory structure for parentDirPath
		
                NarmsConstants narmsConstants = (NarmsConstants)AppContextListener.getSpringContext().getBean("narmsConstants");
                String FILE_PROTOCOL = (String)narmsConstants.getCONSTANTS().get("FILE_PROTOCOL");
                String FILE_HOST = (String)narmsConstants.getCONSTANTS().get("FILE_HOST");
                String REPO_ROOT = (String)narmsConstants.getCONSTANTS().get("REPO_ROOT");
                
		URL parentUrl = new URL(FILE_PROTOCOL, FILE_HOST, 
				REPO_ROOT + (StringUtils.isEmpty(relativeDirPath)?"":relativeDirPath.trim()));
		
                logger.debug("parentUrl := " + parentUrl);
                
                
		File parentDir = Paths.get(parentUrl.toURI()).toFile();
                if( !parentDir.exists()) {
                    parentDir.mkdirs();
		}
		
		
                logger.debug("parentDir := " + parentDir.getAbsolutePath());
		
		File fileToCreate = new File(parentDir, fileName);
		
		// delete of file already exist
		if(fileToCreate.exists()) {
			fileToCreate.delete();
		}
		
		
		// delete file with same name but different extentions
                /***
                 * This will always be same extention
                 
		Collection<File> existingSameNameFiles = FileUtils.listFiles(parentDir, 
				new WildcardFileFilter(FilenameUtils.getBaseName(fileName) + ".*"), null);
		
		if(!CollectionUtils.isEmpty(existingSameNameFiles)) {
			for (File file : existingSameNameFiles) {
				file.delete();
			}
		}
		
		*/
		fileToCreate.createNewFile();
		
		return fileToCreate;
	}
	
}
