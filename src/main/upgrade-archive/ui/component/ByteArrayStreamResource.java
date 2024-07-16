package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
