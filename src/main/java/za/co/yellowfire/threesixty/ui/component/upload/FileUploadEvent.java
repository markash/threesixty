package za.co.yellowfire.threesixty.ui.component.upload;

import java.io.File;
import java.util.EventObject;

public class FileUploadEvent extends EventObject {
    private final File file;
    private final long length;
    private final String mimeType;

    FileUploadEvent(
            final Object source,
            final File file,
            final long length,
            final String mimeType) {

        super(source);
        this.file = file;
        this.length = length;
        this.mimeType = mimeType;
    }

    public File getFile() {
        return file;
    }

    public long getLength() {
        return length;
    }

    public String getMimeType() {
        return mimeType;
    }
}
