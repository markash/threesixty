package za.co.yellowfire.threesixty.domain;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
			  operations.store(is, fileName);
		  }
	  }
	  
	  public File retrieveFile(final String fileName) throws IOException {
		  GridFsResource resource = operations.getResource(fileName);
		  return resource.getFile();
	  }
	  
	  public byte[] retrieveFileContents(final String fileName) throws IOException {
		  GridFsResource resource = operations.getResource(fileName);
		  
		  byte[] buffer = new byte[(int)resource.contentLength()];
		  IOUtils.readFully(resource.getInputStream(), buffer);
		  
		  return buffer;
	  }
}