package za.co.yellowfire.threesixty.domain.question;

import java.io.Serializable;

/**
 * 
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public interface Question<T> extends Serializable {
	/**
	 * Return the phrase of the question
	 * @return The phrase
	 */
	String getPhrase();
	/**
	 * Returns the answer of the question
	 * @return The answer of type T
	 */
	T getAnswer();
	/**
	 * Sets the answer of the question
	 * @param answer The answer of the question of type T
	 * @throws Answer exception if the answer is not valid
	 */
	void setAnswer(final T answer) throws AnswerException;
}
