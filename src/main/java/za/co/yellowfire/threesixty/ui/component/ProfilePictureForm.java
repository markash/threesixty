package za.co.yellowfire.threesixty.ui.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ProfilePictureForm extends VerticalLayout {

	private static final String IMG_PROFILE = "Profile image";
	private static final String BUTTON_UPLOAD = "Upload";
	
	private final Embedded image = new Embedded(IMG_PROFILE);
	private final ImageUploader receiver = new ImageUploader(); 
	private final Upload upload = new Upload("Upload Image Here", receiver);
	
	public ProfilePictureForm(final UploadListener listener) {
		upload.setButtonCaption(BUTTON_UPLOAD);
		upload.addSucceededListener(receiver);
		
		receiver.setUploadListener(listener);
		
		addComponents(upload, image);
	}
	
	class ImageUploader implements Receiver, SucceededListener {
	    private UploadListener listener;
		public File file;
	    
		public void setUploadListener(final UploadListener listener) {
			this.listener = listener;
		}
		
	    public OutputStream receiveUpload(String filename,
	                                      String mimeType) {
	        
	        FileOutputStream fos = null;
	        try {
	            file = File.createTempFile("profile", filename);
	            fos = new FileOutputStream(file);
	        } catch (final java.io.FileNotFoundException e) {
	            new Notification("Could not open file<br/>",
	                             e.getMessage(),
	                             Notification.Type.ERROR_MESSAGE)
	                .show(Page.getCurrent());
	            return null;
	    
		    } catch (final java.io.IOException e) {
	            new Notification("System problem uploading file<br/>",
	                             e.getMessage(),
	                             Notification.Type.ERROR_MESSAGE)
	                .show(Page.getCurrent());
	            return null;
	        }
	        
	        return fos;
	    }

	    public void uploadSucceeded(SucceededEvent event) {
	        // Show the uploaded file in the image viewer
	        image.setVisible(true);
	        //image.setMimeType(mimeType);
	        image.setSource(new FileResource(file));
	        
	        if (listener != null) {
	        	listener.fileUploaded(new FileEvent(file));
	        }
	    }
	};
	
	public interface UploadListener extends Serializable {
		void fileUploaded(FileEvent event);
	}
	
	public static class FileEvent {
		private File file;

		public FileEvent(File file) {
			super();
			this.file = file;
		}

		public File getFile() {
			return file;
		}
	}
}
