package za.co.yellowfire.threesixty.domain.question;

public interface RangedQuestion<T extends Number> extends Question<T> {
	/**
	 * The upper bound of the range
	 * @return The upper bound of the range of type T
	 */
	T getUpperBound();
	/**
	 * The lower bound of the range
	 * @return The lower bound of the range of type T
	 */
	T getLowerBound();
	/**
	 * The upper bound of the range
	 * @param upperBound The upper bound of the range of type T
	 */
	void setUpperBound(final T upperBound);
	/**
	 * The lower bound of the range
	 * @param lowerBound The lower bound of the range of type T
	 */
	void setLowerBound(final T lowerBound);
	/**
	 * The increment of type T from lower bound to upper bound
	 * @return The increment of type T
	 */
	T getIncrement();
	/**
	 * The increment of type T from lower bound to upper bound
	 * @param increment The increment of type T
	 */
	void setIncrement(final T increment);
}
