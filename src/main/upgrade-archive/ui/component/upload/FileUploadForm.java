package za.co.yellowfire.threesixty.ui.component.upload;

import com.vaadin.event.EventRouter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import za.co.yellowfire.threesixty.ui.I8n;

public class FileUploadForm extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private final FileUploadReceiver receiver = new FileUploadReceiver();
	private final Upload upload = new Upload("", receiver);
	private EventRouter eventRouter;

	public FileUploadForm() {

		upload.setButtonCaption(I8n.Button.CHOOSE_FILE);
		upload.addSucceededListener(receiver);
        upload.addSucceededListener(event -> upload.setCaption(event.getFilename()));

		HorizontalLayout buttons = new MHorizontalLayout()
                .withSpacing(true)
                .with(
                        new MButton(VaadinIcons.CHECK_CIRCLE_O, I8n.Button.OK, this::onOk),
                        new MButton(VaadinIcons.CLOSE_CIRCLE_O, I8n.Button.CANCEL, this::onCancel));

		addComponents(
		        upload,
                buttons);

		setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);
	}

	public Registration addFileUploadListener(
			final FileUploadListener listener) {

		return getEventRouter()
				.addListener(
						FileUploadEvent.class,
						listener,
						FileUploadListener.class.getDeclaredMethods()[0]);
	}

	private void onOk(
			final ClickEvent event) {

		getEventRouter()
                .fireEvent(
                        new FileUploadEvent(
                                this,
                                this.receiver.getFile(),
                                this.receiver.getLength(),
                                this.receiver.getMimeType()));

		HasComponents parent = getParent();
		if (parent instanceof Window) {
			((Window) parent).close();
		}
	}

	private void onCancel(
	        final ClickEvent event) {

	    HasComponents parent = getParent();
		if (parent instanceof Window) {
			((Window) parent).close();
		}
	}

	private EventRouter getEventRouter() {

	    if (eventRouter == null) {
			eventRouter = new EventRouter();
		}
		return eventRouter;
	}
}
