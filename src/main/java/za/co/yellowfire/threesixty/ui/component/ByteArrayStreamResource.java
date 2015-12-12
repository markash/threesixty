package za.co.yellowfire.threesixty.ui.component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource;

@SuppressWarnings("serial")
public class ByteArrayStreamResource extends StreamResource {

	public ByteArrayStreamResource(final byte[] content, final String fileName) {
		super(new StreamSource() {
				@Override
				public InputStream getStream() {
					return new ByteArrayInputStream(content);
				}
			}, 
			fileName);
	}
}
