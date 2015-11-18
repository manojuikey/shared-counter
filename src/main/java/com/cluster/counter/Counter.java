
package com.cluster.counter;

/**
 * The Interface Counter. Implementation of this interface must provide the thread safety guarantee in all operations.
 * Implementation should also guarantee the state of counter is preserved in case of whole clustered restarts. 
 * 
 */
public interface Counter {

	/**
	 * Increment and get, increment the counter atomically and returns the updated value.
	 *
	 * @return the incremented value.
	 */
	public int incrementAndGet();

	/**
	 * Decrement and get, decrement the counter atomically and returns the updated value.
	 *
	 * @return the decremented value.
	 */
	public int decrementAndGet();

	/**
	 * Gets the current value of the counter.
	 *
	 * @return the current value
	 */
	public int getCurrentValue();

	/**
	 * Resets the counter to zero.
	 */
	public void reset();
}
