package com.cluster.counter;


/**
 * A factory for creating Counter objects.
 */
public interface CounterFactory {

	/**
	 * Gets the counter.
	 *
	 * @param counterName the counter name
	 * @return the counter instance
	 */
	Counter getCounter(String counterName);
}
