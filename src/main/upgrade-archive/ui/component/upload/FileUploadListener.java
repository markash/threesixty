package za.co.yellowfire.threesixty.ui.component.upload;

import com.vaadin.event.SerializableEventListener;

public interface FileUploadListener extends SerializableEventListener {
    void uploaded(FileUploadEvent event);
}