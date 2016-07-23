package za.co.yellowfire.threesixty.ui.component.field;

/**
 * Converts a S object to a MStatsModel object
 * @author Mark P Ashworth
 */
public interface MStatsConverter<S> {
	MStatsModel convert(final S object);
}
