package za.co.yellowfire.threesixty.ui.component.field;

import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

@SuppressWarnings("serial")
public class PictureSelectionForm extends VerticalLayout {

	private static final String IMG_PREVIEW = "Image preview";
	private static final String BUTTON_UPLOAD = "Upload";
	private static final String BUTTON_OK = "OK";
	private static final String BUTTON_CANCEL = "Cancel";
	
	private final Embedded image = new Embedded(IMG_PREVIEW);
	private final ImageUploader receiver = new ImageUploader(); 
	private final Upload upload = new Upload("", receiver);
	private final Button okButton = ButtonBuilder.build(BUTTON_OK, FontAwesome.CHECK, this::onOk);
	private final Button cancelButton = ButtonBuilder.build(BUTTON_CANCEL, FontAwesome.TIMES_CIRCLE, this::onCancel);
	
	private final ImageListener listener;
	
	public PictureSelectionForm(final ImageListener listener) {
		
		this.listener = listener;
		
		upload.setButtonCaption(BUTTON_UPLOAD);
		upload.addSucceededListener(receiver);
		upload.setImmediate(true);
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		buttons.addComponents(okButton, cancelButton);
		
		addComponents(upload, image, buttons);
		setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);
	}
	
	public void onUpload(final ClickEvent event) {
		upload.startUpload();
	}

	public void onOk(final ClickEvent event) {
		if (listener != null) {
        	listener.imageSelected(new FileEvent(receiver.getFile()));
        }
		
		HasComponents parent = getParent();
		if (parent instanceof Window) {
			((Window) parent).close();
		}
	}
	
	public void onCancel(final ClickEvent event) {
		HasComponents parent = getParent();
		if (parent instanceof Window) {
			((Window) parent).close();
		}
	}
	
	class ImageUploader implements Receiver, SucceededListener {
		public File file;
	    public String mimeType;
	    
		public File getFile() { 
			return this.file;
		}
		
	    public OutputStream receiveUpload(String filename,
	                                      String mimeType) {
	        
	        try {
	        	this.mimeType = mimeType;
	            this.file = File.createTempFile("profile", filename);
	            return new FileOutputStream(file);
	            
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
	    }

	    public void uploadSucceeded(SucceededEvent event) {       
	        image.setVisible(true);
	        image.setSizeUndefined();
	        //image.setMimeType(mimeType);
	        image.setSource(new FileResource(file));
	        if (image.getWidth() > 300) {
	        	image.setWidth(300.0f, Unit.PIXELS);
	        }
	    }
	};
	
	public interface ImageListener extends Serializable {
		void imageSelected(FileEvent event);
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
