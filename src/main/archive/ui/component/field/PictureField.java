package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.field.PictureSelectionForm.ImageListener;
import za.co.yellowfire.threesixty.ui.view.user.PictureSelectionForm;

@SuppressWarnings("serial")
public class PictureField extends VerticalLayout {

	private Image pictureField = ImageBuilder.BLANK();
    private Window pictureWindow;
    private MButton pictureButton = new MButton().withListener(this::onChangePicture);
    
    public PictureField(final String caption, final ImageListener listener) {
    	
    	this.pictureField.setHeight(150.0f, Unit.PIXELS);
		this.pictureButton.setWidth(this.pictureField.getWidth(), this.pictureField.getWidthUnits());
		
		this.pictureWindow = new Window(caption, new PictureSelectionForm(listener));
    }
    
    public void showWindow() {
    	UI.getCurrent().addWindow(pictureWindow);
    }
    
    public void closeWindow() {
    	UI.getCurrent().removeWindow(pictureWindow);
    }
    
    protected void onChangePicture(ClickEvent event) {
		showWindow();
	}
    
    public void setSource(final byte[] contents, final String fileName) {
    	setSource(new ByteArrayStreamResource(contents, fileName));
    }
    
    public void setSource(final Resource resource) {
    	this.pictureField.setSource(resource);
		this.pictureField.markAsDirty();
    }
}
