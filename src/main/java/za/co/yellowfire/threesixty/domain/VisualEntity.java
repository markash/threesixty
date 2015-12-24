package za.co.yellowfire.threesixty.domain;

public interface VisualEntity {
	/**
	 * Returns the picture content of the visual entity
	 * @return The byte array of the picture
	 */
	byte[] getPictureContent();
	/**
	 * Returns the name of the picture contents of the visual entity
	 * @return The name of the picture
	 */
    String getPictureName();
    /**
     * Whether the visual entity has a picture defined
     * @return True if there is picture content else false
     */
    boolean hasPicture();
}
