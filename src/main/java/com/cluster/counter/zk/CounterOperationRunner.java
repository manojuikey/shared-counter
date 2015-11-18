
package com.cluster.counter.zk;

import com.cluster.counter.CounterFactory;
import com.cluster.counter.CounterOperation;
import com.cluster.counter.zk.ZookeeperBaseCounterFactory;

/**
 * The Class CounterOpRunner which uses Zookeeper base implementation of
 * distributed counter.
 */
public class CounterOperationRunner {

	/** The factory. */
	private final CounterFactory factory;

	/**
	 * Instantiates a new counter operation runner
	 *
	 * @param connectionString
	 *            the connection string
	 */
	public CounterOperationRunner(final String connectionString) {
		factory = new ZookeeperBaseCounterFactory(connectionString);
	}

	/**
	 * Execute the operation on counter
	 *
	 * @param operation
	 *            the counter operation object
	 * @return the value of counter post the operation.
	 * @throws Exception
	 *             the exception
	 */
	public int execute(final CounterOperation operation) {

		switch (operation.getOperationType()) {
		case INCREMENT_AND_GET:
			return factory.getCounter(operation.getCounterName()).incrementAndGet();
		case DECREMENT_AND_GET:
			return factory.getCounter(operation.getCounterName()).decrementAndGet();
		default:
			throw new UnsupportedOperationException("operation not supported");
		}
	}
}
