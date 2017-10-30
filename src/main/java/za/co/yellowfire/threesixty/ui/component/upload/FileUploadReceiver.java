package za.co.yellowfire.threesixty.ui.component.upload;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;

import java.io.*;

public class FileUploadReceiver implements Upload.Receiver, Upload.SucceededListener {
    private File file;
    private long length;
    private String mimeType;

    public File getFile() { return this.file; }
    public long getLength() { return length; }
    public String getMimeType() { return mimeType; }

    public OutputStream receiveUpload(
            final String filename,
            final String mimeType) {

        try {
            this.mimeType = mimeType;
            this.file = File.createTempFile("importUserFile", filename);
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

    @Override
    public void uploadSucceeded(
            final Upload.SucceededEvent event) {

        this.length = event.getLength();
    }
}