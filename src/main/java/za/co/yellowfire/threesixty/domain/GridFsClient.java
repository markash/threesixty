package za.co.yellowfire.threesixty.domain;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Component;

@Component
public class GridFsClient {

	  @Autowired
	  GridFsOperations operations;

	  public void storeFile(final File file, final String fileName) throws FileNotFoundException, IOException {
		  operations.delete(new Query(Criteria.where("filename").is(fileName)));
		  try (FileInputStream fs = new FileInputStream(file)) {
			  operations.store(fs, fileName);
		  }
	  }
	  
	  public void storeFile(final byte[] content, final String fileName) throws FileNotFoundException, IOException {
		  operations.delete(new Query(Criteria.where("filename").is(fileName)));
		  try (InputStream is = new ByteArrayInputStream(content)) {
			  
			  String[] parts = fileName.split("\\.");
			  String extension = parts[parts.length - 1];
			  
			  String contentType;
			  if (StringUtils.isBlank(extension)) {
				  contentType = null;
			  } else if (extension.equalsIgnoreCase("png")) {
				  contentType = "image/png";
			  } else if (extension.equalsIgnoreCase("svg")) {
				  contentType = "image/svg";
			  } else if (extension.equalsIgnoreCase("jpg")) {
				  contentType = "image/jpeg";
			  } else if (extension.equalsIgnoreCase("gif")) {
				  contentType = "image/gif";
			  } else if (extension.equalsIgnoreCase("txt")) {
				  contentType = "text/plain";
			  } else if (extension.equalsIgnoreCase("html")) {
				  contentType = "text/html";
			  } else {
				  contentType = null;
			  }
			  
			  operations.store(is, fileName, (String) contentType);
		  }
	  }
	  
	  public File retrieveFile(final String fileName) throws IOException {
		  GridFsResource resource = operations.getResource(fileName);
		  return resource.getFile();
	  }
	  
	  public GridFsResource retrieveResource(final String fileName) throws IOException {
		  return operations.getResource(fileName);
	  }
	  
	  public byte[] retrieveFileContents(final String fileName) throws IOException {
		  GridFsResource resource = operations.getResource(fileName);
		  
		  byte[] buffer = new byte[(int)resource.contentLength()];
		  IOUtils.readFully(resource.getInputStream(), buffer);
		  
		  return buffer;
	  }
}