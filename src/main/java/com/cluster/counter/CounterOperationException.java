package com.cluster.counter;

/**
 * The Class CounterOperationException.
 */
public class CounterOperationException extends RuntimeException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 134234535345L;

	/**
	 * Instantiates a new counter operation exception.
	 *
	 * @param message the message
	 */
	public CounterOperationException(final String message) {
		super(message);
	}

}
